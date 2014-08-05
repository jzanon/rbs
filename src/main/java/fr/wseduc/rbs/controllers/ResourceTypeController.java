package fr.wseduc.rbs.controllers;

import static org.entcore.common.http.response.DefaultResponseHandler.arrayResponseHandler;
import static org.entcore.common.http.response.DefaultResponseHandler.notEmptyResponseHandler;
import static org.entcore.common.http.response.DefaultResponseHandler.defaultResponseHandler;

import org.entcore.common.controller.ControllerHelper;
import org.entcore.common.service.VisibilityFilter;
import org.entcore.common.user.UserInfos;
import org.entcore.common.user.UserUtils;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;

import fr.wseduc.rs.ApiDoc;
import fr.wseduc.rs.Delete;
import fr.wseduc.rs.Get;
import fr.wseduc.rs.Post;
import fr.wseduc.rs.Put;
import fr.wseduc.security.ActionType;
import fr.wseduc.security.SecuredAction;
import fr.wseduc.webutils.http.Renders;
import fr.wseduc.webutils.request.RequestUtils;

public class ResourceTypeController extends ControllerHelper {

	@Get("/types")
	@ApiDoc("List resource types")
	@SecuredAction("rbs.type.list")
	public void listResourceTypes(final HttpServerRequest request){
		UserUtils.getUserInfos(eb, request, new Handler<UserInfos>() {
			@Override
			public void handle(final UserInfos user) {
				crudService.list(VisibilityFilter.OWNER_AND_SHARED, user, arrayResponseHandler(request));
			}
		});
	}
	
	@Get("/type/:id")
	@ApiDoc("Get resource type")
	@SecuredAction(value = "rbs.read", type = ActionType.RESOURCE)
	public void getResourceType(final HttpServerRequest request){
		super.retrieve(request);
	}
	
	@Post("/type")
	@ApiDoc("Create resource type")
	@SecuredAction("rbs.type.create")
	public void createResourceType(final HttpServerRequest request) {
		UserUtils.getUserInfos(eb, request, new Handler<UserInfos>() {
			@Override
			public void handle(final UserInfos user) {
				if (user != null) {
					RequestUtils.bodyToJson(request, pathPrefix + "createResourceType",  new Handler<JsonObject>() {
						@Override
						public void handle(JsonObject object) {
							crudService.create(object, user, notEmptyResponseHandler(request));
						}
					});
				} else {
					log.debug("User not found in session.");
					Renders.unauthorized(request);
				}
			}
		});
	}
	
	@Put("/type/:id")
	@ApiDoc("Update resource type")
	@SecuredAction(value = "rbs.manager", type = ActionType.RESOURCE)
	public void updateResourceType(final HttpServerRequest request) {
		UserUtils.getUserInfos(eb, request, new Handler<UserInfos>() {
			@Override
			public void handle(final UserInfos user) {
				if (user != null) {
					RequestUtils.bodyToJson(request, pathPrefix + "updateResourceType", new Handler<JsonObject>() {
						@Override
						public void handle(JsonObject object) {
							String id = request.params().get("id");
							crudService.update(id, object, user, defaultResponseHandler(request));
						}
					});
				} else {
					log.debug("User not found in session.");
					Renders.unauthorized(request);
				}
			}
		});
	}

	@Delete("/type/:id")
	@ApiDoc("Delete resource type")
	@SecuredAction(value = "rbs.manager", type = ActionType.RESOURCE)
	public void deleteResourceType(final HttpServerRequest request) {
		UserUtils.getUserInfos(eb, request, new Handler<UserInfos>() {
			@Override
			public void handle(final UserInfos user) {
				if (user != null) {
					String id = request.params().get("id");
					crudService.delete(id, user, defaultResponseHandler(request, 204));
				} else {
					log.debug("User not found in session.");
					Renders.unauthorized(request);
				}
			}
		});
	}
	
	@Get("/type/share/json/:id")
	@ApiDoc("List rights for a given resource type")
	@SecuredAction(value = "rbs.manager", type = ActionType.RESOURCE)
	public void shareJson(final HttpServerRequest request){
		super.shareJson(request, false);
	}

	@Put("/type/share/json/:id")
	@ApiDoc("Add rights for a given resource type")
	@SecuredAction(value = "rbs.manager", type = ActionType.RESOURCE)
	public void shareJsonSubmit(final HttpServerRequest request){
		super.shareJsonSubmit(request, null, false);
	}

	@Put("/type/share/remove/:id")
	@ApiDoc("Remove rights for a given resource type")
	@SecuredAction(value = "rbs.manager", type = ActionType.RESOURCE)
	public void removeShare(final HttpServerRequest request){
		super.removeShare(request, false);
	}
	
}