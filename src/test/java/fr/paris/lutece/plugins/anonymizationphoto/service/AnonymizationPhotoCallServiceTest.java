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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fr.paris.lutece.plugins.anonymizationphoto.business.AnonymizationPhotoStatsDAO;
import fr.paris.lutece.plugins.anonymizationphoto.business.PhotoToAnonymized;
import fr.paris.lutece.plugins.anonymizationphoto.business.ResponseAnonymizationPhoto;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

@RunWith( PowerMockRunner.class )
@PrepareForTest( {
        AppPropertiesService.class, SpringContextService.class, AnonymizationPhotoCallService.class
} )
public class AnonymizationPhotoCallServiceTest
{

    @Test
    public void testCallServiceAnonymization( ) throws Exception
    {

        // init mock
        PowerMockito.mockStatic( AppPropertiesService.class );
        Mockito.when( AppPropertiesService.getProperty( "anonymizationphoto.anonymization.service.url" ) ).thenReturn( "fakeUrl" );
        Mockito.when( AppPropertiesService.getProperty( "anonymizationphoto.application.code" ) ).thenReturn( "fakeCode" );

        AnonymizationPhotoStatsDAO mockDao = Mockito.mock( AnonymizationPhotoStatsDAO.class );
        Mockito.doNothing( ).when( mockDao ).insert( ( Mockito.any( ResponseAnonymizationPhoto.class ) ) );
        PowerMockito.mockStatic( SpringContextService.class );
        Mockito.when( SpringContextService.getBean( "anonymizationPhotoStatsDAO" ) ).thenReturn( mockDao );

        // run test
        PhotoToAnonymized photoToAnonymized = new PhotoToAnonymized( );
        photoToAnonymized.setIdResourceAnonymized( 1 );
        AnonymizationPhotoCallService spyAnonymizationPhotoCallService = PowerMockito.spy( new AnonymizationPhotoCallService( ) );
        PowerMockito.doReturn( jsonResponse( ) )
                .when( spyAnonymizationPhotoCallService, MemberMatcher.method( AnonymizationPhotoCallService.class, "callRestWs", PhotoToAnonymized.class ) )
                .withArguments( photoToAnonymized );
        ResponseAnonymizationPhoto response = spyAnonymizationPhotoCallService.callServiceAnonymization( photoToAnonymized );

        // assert
        assertNotNull( response );
        assertEquals( 1, response.getIdResource( ) );
        assertNotNull( response.getPhotoAnonymized( ) );
        assertEquals( 3, response.getNbFacesDetected( ) );

        // test exception
        AnonymizationPhotoCallService spyAnonymizationPhotoCallServiceExeption = PowerMockito.spy( new AnonymizationPhotoCallService( ) );
        PowerMockito.doThrow( new HttpAccessException( "Exeption", null ) ).when( spyAnonymizationPhotoCallServiceExeption,
                MemberMatcher.method( AnonymizationPhotoCallService.class, "callRestWs", PhotoToAnonymized.class ) ).withArguments( photoToAnonymized );
        response = spyAnonymizationPhotoCallServiceExeption.callServiceAnonymization( photoToAnonymized );

        // assert
        assertNotNull( response );
        assertEquals( "Exeption", response.getErrorMessage( ) );
    }

    private String jsonResponse( )
    {
        return "{'app_code':'AppCode','anonymized_photo':'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/4QAiRXhpZgAATU0AKgAAAAgAAQESAAMAAAABAAEAAAAAAAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAgMDAwYDAwYMCAcIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAz/wAARCAABAAEDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD7UooorM9A/9k=','total_nb_faces': 3,'elapsed_time_in_seconds': 34.87054681777954}";
    }

}
