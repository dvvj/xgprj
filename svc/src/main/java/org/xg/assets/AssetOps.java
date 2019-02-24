package org.xg.assets;

import org.xg.CustomerOps;
import org.xg.SvcUtils;
import org.xg.gnl.GlobalCfg;
import org.xg.svc.ImageInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

@Path("asset")
public class AssetOps {

  private final static Logger logger = Logger.getLogger(AssetOps.class.getName());

  @POST
  @Path("img")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces("image/png")
  public Response getImage(String imgInfoJson) {
    ImageInfo imgInfo = ImageInfo.fromJson(imgInfoJson);
    String imgPath = imgInfo.localPath(SvcUtils.getCfg().assetLocalPath());
    File img = new File(imgPath);
    logger.warning("Getting image from: " + img.getAbsolutePath());
    Response.ResponseBuilder response = Response.ok(img);
    response.header("Content-Disposition",
      "attachment; filename=image_from_server.png");
    return response.build();
  }

}
