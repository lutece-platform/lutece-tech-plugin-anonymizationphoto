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
package fr.paris.lutece.plugins.anonymizationphoto.service.daemon;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.anonymizationphoto.business.IAnonymizationPhotoDAO;
import fr.paris.lutece.plugins.anonymizationphoto.business.PhotoToAnonymized;
import fr.paris.lutece.plugins.anonymizationphoto.business.ResponseAnonymizationPhoto;
import fr.paris.lutece.plugins.anonymizationphoto.service.AnonymizationPhotoCallService;
import fr.paris.lutece.plugins.anonymizationphoto.service.IAnonymizationPhotoService;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class AnonymizationPhoto extends Daemon
{

    // PROPERTY
    private static final int PROPERTY_MAX_NUMBER_PHOTOS = AppPropertiesService.getPropertyInt( "anonymizationphoto.daemon.max.photo", 10 );

    private IAnonymizationPhotoService _anonymizationPhotoService = SpringContextService.getBean( "anonymizationPhotoService" );
    private IAnonymizationPhotoDAO _anonymizationPhotoDao = SpringContextService.getBean( "anonymizationPhotoDAO" );

    private AnonymizationPhotoCallService _anonymizationPhotoCallService = SpringContextService.getBean( "anonymizationPhotoCallService" );

    StringBuilder _sbLogs;

    @Override
    public void run( )
    {
        _sbLogs = new StringBuilder( );

        _sbLogs.append( "Anonymization Photo - \n" );

        List<PhotoToAnonymized> listAnonymizationPhoto = _anonymizationPhotoDao.loadPhotos( );

        if ( ( listAnonymizationPhoto == null ) || listAnonymizationPhoto.isEmpty( ) )
        {
            AppLogService.info( "No photo loaded !! " );
            return;
        }
        if ( listAnonymizationPhoto.size( ) > PROPERTY_MAX_NUMBER_PHOTOS )
        {
            AppLogService.error( "Number photos loaded greather than anonymizationphoto.daemon.max.photo !! " );
            return;
        }

        _anonymizationPhotoService.preTreatment( listAnonymizationPhoto );

        List<ResponseAnonymizationPhoto> listResponse = new ArrayList<>( );
        for ( PhotoToAnonymized photoToAnonymized : listAnonymizationPhoto )
        {
            try
            {
                ResponseAnonymizationPhoto response = _anonymizationPhotoCallService.callServiceAnonymization( photoToAnonymized );
                listResponse.add( response );
                _anonymizationPhotoService.postTreatment( response );

                if ( StringUtils.isBlank( response.getErrorMessage( ) ) && ( response.getPhotoAnonymized( ) != null ) )
                {
                    _sbLogs.append( " photo with resource id " + response.getIdResource( ) + " have been anonymized \n" );
                }
                else
                {
                    _sbLogs.append( "Error with resource id " + response.getIdResource( ) + " : " + response.getErrorMessage( ) + "\n" );
                }
            }
            catch( Exception e )
            {
                AppLogService.error( "Error processing photo id : " + photoToAnonymized.getlIdResourceAnonymized( ) + "\n" );
            }
        }

        _anonymizationPhotoService.postTreatment( listResponse );

        AppLogService.info( _sbLogs.toString( ) );

        setLastRunLogs( _sbLogs.toString( ) );
    }

}
