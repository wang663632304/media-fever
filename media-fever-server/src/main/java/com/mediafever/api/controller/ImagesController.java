package com.mediafever.api.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.jdroid.javaweb.repository.FileEntityRepository;

/**
 * 
 * @author Maxi Rosson
 */
@Path("/images")
@Controller
public class ImagesController {
	
	@Autowired
	private FileEntityRepository fileEntityRepository;
	
	@GET
	@Path("{name}")
	@Produces("image/*")
	public byte[] getImage(@PathParam("name") String name) {
		return fileEntityRepository.getByName(name).getContent();
	}
}
