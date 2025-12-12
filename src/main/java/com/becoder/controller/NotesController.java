package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.NotesDto;
import com.becoder.service.NoteService;
import com.becoder.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {

	@Autowired
	private NoteService noteService;

	@PostMapping("/")
	public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)
			throws Exception {

		Boolean saveNotes = noteService.saveNotes(notes, file);

		if (saveNotes) {
			return CommonUtil.createBuildResponseMessage("Notes saved success", HttpStatus.CREATED);
		}

		return CommonUtil.createBuildResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@GetMapping("/")
	public ResponseEntity<?> getAllNotes() throws Exception {
		List<NotesDto> notes = noteService.getAllNotes();

		if (CollectionUtils.isEmpty(notes)) {
			return ResponseEntity.noContent().build();
		}

		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}

}
