/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is Web Questionnaires 2
 *
 * The Initial Owner of the Original Code is European Environment
 * Agency. Portions created by TripleDev are Copyright
 * (C) European Environment Agency.  All Rights Reserved.
 *
 * Contributor(s):
 *        Anton Dmitrijev
 */
package eionet.webq.service;

import eionet.webq.dao.orm.ProjectFile;
import eionet.webq.dao.orm.UserFile;

import java.util.Collection;

/**
 * Service for storing uploaded files.
 */
public interface UserFileService {
    /**
     * Save data from uploaded file to storage.
     *
     * @param file uploaded file to be saved to storage
     * @return auto generated id of inserted record
     */
    int save(UserFile file);

    /**
     * Save data from uploaded file to storage.
     *
     * @param file uploaded file to be saved to storage
     * @param webForm web form
     * @return auto generated id of inserted record
     * @throws eionet.webq.service.FileNotAvailableException if remote file not available
     */
    int saveBasedOnWebForm(UserFile file, ProjectFile webForm) throws FileNotAvailableException;

    /**
     * Fetches uploaded file from storage by specified id. User access to this file must be checked. Only
     * {@link eionet.webq.dao.orm.UserFile#getName()} and {@link eionet.webq.dao.orm.UserFile#getContent()} will be set.
     *
     * @param id file id
     * @return uploaded file
     */
    UserFile getById(int id);

    /**
     * Fetches uploaded file from storage by specified id and user session id. User access to this file must be checked. Only
     * {@link eionet.webq.dao.orm.UserFile#getName()} and {@link eionet.webq.dao.orm.UserFile#getContent()} will be set.
     *
     * @param id file id
     * @param userId user id md5 hash of jsessionid
     * @return uploaded file
     */
    UserFile getByIdAndUser(int id, String userId);

    /**
     * Keeps track on file download by user.
     *
     * @param id file id
     * @return user file
     */
    UserFile download(int id);

    /**
     * All uploaded files by current session user. {@link eionet.webq.dao.orm.UserFile#getContent()} is not included into resulting
     * collection. Use {@link UserFileService#getById(int)} for fetching specific file content.
     *
     * @return All uploaded files by current session user.
     */
    Collection<UserFile> allUploadedFiles();

    /**
     * Update file content and updated time in storage. Only {@link eionet.webq.dao.orm.UserFile#getContent()},
     * {@link eionet.webq.dao.orm.UserFile#getSizeInBytes()} and {@link eionet.webq.dao.orm.UserFile#updated} will be set.
     *
     * @param file content to be updated in storage
     */
    void updateContent(UserFile file);

    /**
     * Update file without changing content and last updated time.
     *
     * @param file to be updated in storage
     */
    void update(UserFile file);

    /**
     * Removes files by id.
     *
     * @param fileIds files to be removed.
     */
    void removeFilesById(int[] fileIds);

    /**
     * Updates userId in storage.
     *
     * @param oldUserId old user id.
     * @param newUserId new user id.
     */
    void updateUserId(String oldUserId, String newUserId);
}
