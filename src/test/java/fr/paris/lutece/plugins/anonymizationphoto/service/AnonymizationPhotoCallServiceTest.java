/*
 * Copyright (c) 2002-2019, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.anonymizationphoto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.anonymizationphoto.business.AnonymizationPhotoStatsDAO;
import fr.paris.lutece.plugins.anonymizationphoto.business.PhotoToAnonymized;
import fr.paris.lutece.plugins.anonymizationphoto.business.ResponseAnonymizationPhoto;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.ReflectionTestUtils;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import jakarta.enterprise.inject.Vetoed;

/**
 * Tests the call to the anonymization service.
 */
public class AnonymizationPhotoCallServiceTest extends LuteceTestCase
{
    private static final String JSON_RESPONSE_VALUE = "{\"app_code\":\"AppCode\",\"anonymized_photo\":\"data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/4QAiRXhpZgAATU0AKgAAAAgAAQESAAMAAAABAAEAAAAAAAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAgMDAwYDAwYMCAcIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAz/wAARCAABAAEDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD7UooorM9A/9k=\",\"total_nb_faces\": 3,\"elapsed_time_in_seconds\": 34.87054681777954}";

    /**
     * A valid response is parsed and its statistic saved.
     */
    @Test
    public void testCallServiceAnonymization( )
    {
        RecordingStatsDAO dao = new RecordingStatsDAO( );
        AnonymizationPhotoCallService service = new AnonymizationPhotoCallService( )
        {
            @Override
            String callRestWs( PhotoToAnonymized photoToAnonymized )
            {
                return JSON_RESPONSE_VALUE;
            }
        };
        ReflectionTestUtils.setField( service, AnonymizationPhotoCallService.class, "_anonymizationPhotoStatsDao", dao, AnonymizationPhotoStatsDAO.class );

        ResponseAnonymizationPhoto response = service.callServiceAnonymization( photo( ) );

        assertNotNull( response );
        assertNull( response.getErrorMessage( ) );
        assertEquals( 1, response.getIdResource( ) );
        assertNotNull( response.getPhotoAnonymized( ) );
        assertEquals( 3, response.getNbFacesDetected( ) );
        assertEquals( "34.87054681777954", response.getElapsedTimeSeconde( ) );
        assertEquals( 1, dao._listInserted.size( ) );
    }

    /**
     * A failing call returns its error and saves nothing.
     */
    @Test
    public void testCallServiceAnonymizationError( )
    {
        RecordingStatsDAO dao = new RecordingStatsDAO( );
        AnonymizationPhotoCallService service = new AnonymizationPhotoCallService( )
        {
            @Override
            String callRestWs( PhotoToAnonymized photoToAnonymized ) throws HttpAccessException
            {
                throw new HttpAccessException( "Exeption", null );
            }
        };
        ReflectionTestUtils.setField( service, AnonymizationPhotoCallService.class, "_anonymizationPhotoStatsDao", dao, AnonymizationPhotoStatsDAO.class );

        ResponseAnonymizationPhoto response = service.callServiceAnonymization( photo( ) );

        assertNotNull( response );
        assertEquals( "Exeption", response.getErrorMessage( ) );
        assertEquals( 0, dao._listInserted.size( ) );
    }

    /**
     * Builds the photo sent to the service.
     *
     * @return the photo
     */
    private static PhotoToAnonymized photo( )
    {
        PhotoToAnonymized photoToAnonymized = new PhotoToAnonymized( );
        photoToAnonymized.setIdResourceAnonymized( 1 );
        photoToAnonymized.setPhotoContent( new byte [ ] {
                1
        } );
        return photoToAnonymized;
    }

    /**
     * Statistics DAO keeping the inserted rows in memory.
     */
    @Vetoed
    private static class RecordingStatsDAO extends AnonymizationPhotoStatsDAO
    {
        private final List<ResponseAnonymizationPhoto> _listInserted = new ArrayList<>( );

        /**
         * Records the statistic.
         *
         * @param response
         *            the statistic
         */
        @Override
        public void insert( ResponseAnonymizationPhoto response )
        {
            _listInserted.add( response );
        }
    }
}
