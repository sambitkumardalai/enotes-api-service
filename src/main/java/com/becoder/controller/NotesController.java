package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.FavouriteNoteDto;
import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.endpoint.NotesEndpoint;
import com.becoder.entity.FileDetails;
import com.becoder.service.NoteService;
import com.becoder.util.CommonUtil;

@RestController

public class NotesController implements NotesEndpoint {

	@Autowired
	private NoteService noteService;

	@Override
	public ResponseEntity<?> saveNotes(String notes, MultipartFile file)
			throws Exception {

		Boolean saveNotes = noteService.saveNotes(notes, file);

		if (saveNotes) {
			return CommonUtil.createBuildResponseMessage("Notes saved success", HttpStatus.CREATED);
		}

		return CommonUtil.createBuildResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	public ResponseEntity<?> getAllNotes() throws Exception {
		List<NotesDto> notes = noteService.getAllNotes();

		if (CollectionUtils.isEmpty(notes)) {
			return ResponseEntity.noContent().build();
		}

		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> searchNotes(String key,
			Integer pageNo,
			Integer pageSize) {
		NotesResponse notes = noteService.getNotesByUserSearch(pageNo, pageSize, key);
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> downloadFile(Integer id) throws Exception {
		FileDetails fileDetails = noteService.getFileDetails(id);
		byte[] data = noteService.downloadFile(fileDetails);

		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

		return ResponseEntity.ok().headers(headers).body(data);

	}

	@Override
	public ResponseEntity<?> getAllNotes(Integer pageNo, Integer pageSize

	) throws Exception {
		NotesResponse notes = noteService.getAllNotesByUser(pageNo, pageSize);
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteNotes(Integer id) throws Exception {
		noteService.softDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> restoreNotes(Integer id) throws Exception {
		noteService.restoreNotes(id);
		return CommonUtil.createBuildResponseMessage("Restore Success", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {

		List<NotesDto> notes = noteService.getUserRecycleBinNotes();

		if (CollectionUtils.isEmpty(notes)) {
			return CommonUtil.createBuildResponseMessage("Notes not available in recycle bin.", HttpStatus.OK);
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> hardDeleteNotes(Integer id) throws Exception {
		noteService.hardDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> emptyRecycleBin() throws Exception {
		noteService.emptyRecycleBin();
		return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> favoriteNote(Integer noteId) throws Exception {
		noteService.favoriteNotes(noteId);
		return CommonUtil.createBuildResponseMessage("Notes added Favorite", HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> unFavoriteNote(Integer favNotId) throws Exception {
		noteService.unFavoriteNotes(favNotId);
		return CommonUtil.createBuildResponseMessage("Remove Favorite", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getUserfavoriteNote() throws Exception {

		List<FavouriteNoteDto> userFavoriteNotes = noteService.getUserFavoriteNotes();
		if (CollectionUtils.isEmpty(userFavoriteNotes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(userFavoriteNotes, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> copyNotes(Integer id) throws Exception {
		Boolean copyNotes = noteService.copyNotes(id);

		if (copyNotes) {
			return CommonUtil.createBuildResponseMessage("Copied success", HttpStatus.CREATED);
		} else {
			return CommonUtil.createBuildResponseMessage("Notes added Favorite", HttpStatus.CREATED);
		}
	}
}
