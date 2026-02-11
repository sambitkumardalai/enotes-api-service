package com.becoder.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.FavouriteNoteDto;
import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.entity.FileDetails;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.CategoryRepository;
import com.becoder.repository.NotesRepository;

public interface NoteService {

	public Boolean saveNotes(String notes, MultipartFile file) throws Exception;

	public List<NotesDto> getAllNotes() throws Exception;

	public FileDetails getFileDetails(Integer id) throws Exception;

	public byte[] downloadFile(FileDetails fileDtls) throws Exception;

	public NotesResponse getAllNotesByUser( Integer pageNo, Integer pageSize);

	public void softDeleteNotes(Integer id) throws Exception;

	public void restoreNotes(Integer id) throws Exception;

	public List<NotesDto> getUserRecycleBinNotes();

	public void hardDeleteNotes(Integer id) throws Exception;

	public void emptyRecycleBin();

	public void favoriteNotes(Integer noteId) throws Exception;

	public void unFavoriteNotes(Integer noteId) throws Exception;

	public List<FavouriteNoteDto> getUserFavoriteNotes() throws Exception;
	public Boolean copyNotes(Integer id) throws Exception;
}
