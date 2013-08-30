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
package eionet.webq.dao;

import eionet.webq.dao.util.AbstractLobPreparedStatementCreator;
import eionet.webq.dto.UploadedFile;
import eionet.webq.dto.UserFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * {@link FileStorage} implementation for user files.
 */
@Repository
@Qualifier("user-files")
public class UserFileStorageImpl extends AbstractDao<UserFile> implements FileStorage<String, UserFile> {
    /**
     * {@link JdbcTemplate} to perform data access operations.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * Large objects handles. Used for storing and retrieving {@link java.sql.Blob} object from database.
     */
    @Autowired
    private LobHandler lobHandler;

    @Override
    public int save(final UserFile file, final String userId) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new AbstractLobPreparedStatementCreator(lobHandler, sqlProperties.getProperty("insert.user.file"), "id") {
                    @Override
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
                        ps.setString(1, userId);
                        ps.setString(2, file.getName());
                        ps.setString(3, file.getXmlSchema());
                        lobCreator.setBlobAsBytes(ps, 4, file.getContent());
                        ps.setLong(5, file.getSizeInBytes());
                    }
                }
                , keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public UserFile fileContentBy(int id, String userId) {
        Object[] params = {id, userId};
        return jdbcTemplate.queryForObject(sqlProperties.getProperty("select.user.file.content"), params,
                new RowMapper<UserFile>() {
                    @Override
                    public UserFile mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new UserFile(new UploadedFile(rs.getString(1), lobHandler.getBlobAsBytes(rs, 2)), null);
                    }
                });
    }

    @Override
    public void update(final UserFile file, final String userId) {
        jdbcTemplate.execute(sqlProperties.getProperty("update.user.file"),
                new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
                    @Override
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
                        lobCreator.setBlobAsBytes(ps, 1, file.getContent());
                        ps.setLong(2, file.getSizeInBytes());
                        ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                        ps.setInt(4, file.getId());
                        ps.setString(5, userId);
                    }
                });
    }

    @Override
    public Collection<UserFile> allFilesFor(String userId) {
        return jdbcTemplate.query(sqlProperties.getProperty("select.all.file.for.user"), new Object[] {userId}, rowMapper());
    }

    @Override
    public void remove(final String userId, final int... id) {
        jdbcTemplate.batchUpdate(sqlProperties.getProperty("delete.user.file"), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, id[i]);
                ps.setString(2, userId);
            }

            @Override
            public int getBatchSize() {
                return id.length;
            }
        });
    }

    @Override
    public UserFile fileContentBy(String name, String userId) {
        throw new UnsupportedOperationException("File name and userId pair is not unique in storage");
    }

    @Override
    public UserFile fileById(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    Class<UserFile> getDtoClass() {
        return UserFile.class;
    }
}
