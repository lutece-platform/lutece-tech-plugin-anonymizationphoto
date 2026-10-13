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

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import fr.paris.lutece.plugins.anonymizationphoto.business.AnonymizationPhotoStatsDAO;
import fr.paris.lutece.plugins.anonymizationphoto.business.PhotoToAnonymized;
import fr.paris.lutece.plugins.anonymizationphoto.business.ResponseAnonymizationPhoto;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

/**
 * Class Service to call abonymization photo service.
 *
 * @author vincent.corbel
 *
 */
@ApplicationScoped
public class AnonymizationPhotoCallService
{

    // PROPERTY
    private static final String PROPERTY_SERVICE_ANONYMIZATION_URL = "anonymizationphoto.anonymization.service.url";
    private static final String PROPERTY_APPLICATION_CODE = "anonymizationphoto.application.code";

    private static final String BASE64 = ";base64,";
    private static final String DEFAULT_MIME_TYPE = "data:image/jpg";

    private static final ObjectMapper MAPPER = new ObjectMapper( );

    @Inject
    private AnonymizationPhotoStatsDAO _anonymizationPhotoStatsDao;

    @Inject
    @ConfigProperty( name = PROPERTY_SERVICE_ANONYMIZATION_URL )
    private Optional<String> _strServiceUrl;

    @Inject
    @ConfigProperty( name = PROPERTY_APPLICATION_CODE )
    private Optional<String> _strApplicationCode;

    /**
     * Call Service to anonymized photo.
     *
     * @param photoToAnonymized
     *            photo to anonymized
     * @return ResponseAnonymizationPhoto
     */
    public ResponseAnonymizationPhoto callServiceAnonymization( PhotoToAnonymized photoToAnonymized )
    {
        ResponseAnonymizationPhoto response = new ResponseAnonymizationPhoto( );
        response.setdResource( photoToAnonymized.getlIdResourceAnonymized( ) );
        try
        {
            String strReponse = callRestWs( photoToAnonymized );
            buildResponseObject( strReponse, response );

            if ( StringUtils.isBlank( response.getErrorMessage( ) ) && ( response.getPhotoAnonymized( ) != null ) )
            {
                _anonymizationPhotoStatsDao.insert( response );
            }

            return response;

        }
        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
            response.setErrorMessage( e.getMessage( ) );
            return response;
        }
    }

    /**
     * Call Rest webservice.
     *
     * @param photoToAnonymized
     *            photo to anonymized
     *
     * @return webservice response
     *
     * @throws HttpAccessException
     *             if the webservice call fails
     */
    String callRestWs( PhotoToAnonymized photoToAnonymized ) throws HttpAccessException
    {

        HttpAccess httpAccess = new HttpAccess( );
        Map<String, String> headersRequest = new HashMap<>( );
        headersRequest.put( "Content-Type", "application/json; charset=UTF-8" );
        Map<String, String> headersResponse = new HashMap<>( );

        ObjectNode jsonContent = MAPPER.createObjectNode( );
        jsonContent.put( "photo", transformPhotoToStringBase64( photoToAnonymized ) );
        jsonContent.put( "app_code", _strApplicationCode.orElse( StringUtils.EMPTY ) );

        return httpAccess.doPostJSON( _strServiceUrl.orElse( null ), jsonContent.toString( ), headersRequest, headersResponse );
    }

    /**
     * Transform photo to Base64.
     *
     * @param photoToAnonymized
     *            photo to transform
     * @return string photo base 64
     */
    private String transformPhotoToStringBase64( PhotoToAnonymized photoToAnonymized )
    {
        String mimeType = ( photoToAnonymized.getMimeType( ) == null ) ? DEFAULT_MIME_TYPE + BASE64 : "data:" + photoToAnonymized.getMimeType( ) + BASE64;
        return mimeType + Base64.getEncoder( ).encodeToString( photoToAnonymized.getPhotoContent( ) );
    }

    /**
     * Build response object
     *
     * @param strReponse
     *            response json format
     * @param response
     *            response object
     * @throws JsonProcessingException
     *             if the response is not valid JSON
     */
    private void buildResponseObject( String strReponse, ResponseAnonymizationPhoto response ) throws JsonProcessingException
    {
        JsonNode jsonResponse = MAPPER.readTree( strReponse );
        String strBase64Photo = jsonResponse.get( "anonymized_photo" ).asText( );

        response.setNbFacesDetected( jsonResponse.get( "total_nb_faces" ).asInt( ) );
        response.setElapsedTimeSeconde( String.valueOf( jsonResponse.get( "elapsed_time_in_seconds" ).asDouble( ) ) );

        String [ ] array = strBase64Photo.split( BASE64 );
        byte [ ] photoDecode = java.util.Base64.getDecoder( ).decode( array [1] );

        response.setPhotoAnonymized( photoDecode );
    }

}
