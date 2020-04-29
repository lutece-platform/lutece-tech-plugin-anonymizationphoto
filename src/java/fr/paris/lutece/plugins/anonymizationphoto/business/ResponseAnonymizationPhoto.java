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
package fr.paris.lutece.plugins.anonymizationphoto.business;

import java.io.Serializable;

public class ResponseAnonymizationPhoto implements Serializable
{
    /**
     * Serial version
     */
    private static final long serialVersionUID = 1L;

    /**
     * id resource to anonymized.
     */
    private int _nIdResource;

    /**
     * Date of anonymization.
     */
    private String _strDateAnonymized;

    /**
     * photo to anonymized
     */
    private byte [ ] _photoAnonymized;

    /**
     * time to anonymized photo
     */
    private String _strElapsedTimeSeconde;

    /**
     * Faces detected on original photo
     */
    private int _nNbFacesDetected;

    /**
     * error message.
     */
    private String _strErrorMessage;

    public byte [ ] getPhotoAnonymized( )
    {
        return _photoAnonymized;
    }

    public int getIdResource( )
    {
        return _nIdResource;
    }

    public String getErrorMessage( )
    {
        return _strErrorMessage;
    }

    public void setdResource( int idResource )
    {
        _nIdResource = idResource;
    }

    public void setErrorMessage( String errorMessage )
    {
        _strErrorMessage = errorMessage;
    }

    public void setPhotoAnonymized( byte [ ] photoAnonymized )
    {
        _photoAnonymized = photoAnonymized;
    }

    public String getElapsedTimeSeconde( )
    {
        return _strElapsedTimeSeconde;
    }

    public void setElapsedTimeSeconde( String elapsedTimeSeconde )
    {
        _strElapsedTimeSeconde = elapsedTimeSeconde;
    }

    public int getNbFacesDetected( )
    {
        return _nNbFacesDetected;
    }

    public void setNbFacesDetected( int nbFacesDetected )
    {
        _nNbFacesDetected = nbFacesDetected;
    }

    public String getDateAnonymized( )
    {
        return _strDateAnonymized;
    }

    public void setDateAnonymized( String dateAnonymized )
    {
        _strDateAnonymized = dateAnonymized;
    }

}
