package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.FavouriteNoteDto;
import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.entity.FileDetails;
import com.becoder.service.NoteService;
import com.becoder.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {

	@Autowired
	private NoteService noteService;

	@PostMapping("/")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)
			throws Exception {

		Boolean saveNotes = noteService.saveNotes(notes, file);

		if (saveNotes) {
			return CommonUtil.createBuildResponseMessage("Notes saved success", HttpStatus.CREATED);
		}

		return CommonUtil.createBuildResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@GetMapping("/")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllNotes() throws Exception {
		List<NotesDto> notes = noteService.getAllNotes();

		if (CollectionUtils.isEmpty(notes)) {
			return ResponseEntity.noContent().build();
		}

		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}

	@GetMapping("/download/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception {
		FileDetails fileDetails = noteService.getFileDetails(id);
		byte[] data = noteService.downloadFile(fileDetails);

		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

		return ResponseEntity.ok().headers(headers).body(data);

	}

	@GetMapping("/user-notes")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getAllNotes(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize

	) throws Exception {
		NotesResponse notes = noteService.getAllNotesByUser(pageNo, pageSize);
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}

	@GetMapping("/delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception {
		noteService.softDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
	}

	@GetMapping("/restore/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception {
		noteService.restoreNotes(id);
		return CommonUtil.createBuildResponseMessage("Restore Success", HttpStatus.OK);
	}

	@GetMapping("/recycle-bin")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {

		List<NotesDto> notes = noteService.getUserRecycleBinNotes();

		if (CollectionUtils.isEmpty(notes)) {
			return CommonUtil.createBuildResponseMessage("Notes not available in recycle bin.", HttpStatus.OK);
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception {
		noteService.hardDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> emptyRecycleBin() throws Exception { 
		noteService.emptyRecycleBin();
		return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
	}

	@GetMapping("/fav/{noteId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> favoriteNote(@PathVariable Integer noteId) throws Exception {
		noteService.favoriteNotes(noteId);
		return CommonUtil.createBuildResponseMessage("Notes added Favorite", HttpStatus.CREATED);
	}

	@DeleteMapping("/un-fav/{favNotId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> unFavoriteNote(@PathVariable Integer favNotId) throws Exception {
		noteService.unFavoriteNotes(favNotId);
		return CommonUtil.createBuildResponseMessage("Remove Favorite", HttpStatus.OK);
	}

	@GetMapping("/fav-note")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserfavoriteNote() throws Exception {

		List<FavouriteNoteDto> userFavoriteNotes = noteService.getUserFavoriteNotes();
		if (CollectionUtils.isEmpty(userFavoriteNotes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(userFavoriteNotes, HttpStatus.OK);
	}

	@GetMapping("/copy/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception {
		Boolean copyNotes = noteService.copyNotes(id);

		if (copyNotes) {
			return CommonUtil.createBuildResponseMessage("Copied success", HttpStatus.CREATED);
		} else {
			return CommonUtil.createBuildResponseMessage("Notes added Favorite", HttpStatus.CREATED);
		}
	}
}
