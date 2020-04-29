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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.sql.DAOUtil;

public class AnonymizationPhotoStatsDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT nextval('seq_anonymizationphoto_stats_id')";
    private static final String SQL_QUERY_SELECT = "SELECT id_resource_anonymized, date_anonymization, elapsed_time_in_seconds, faces_detected FROM anonymizationphoto_stats ORDER BY id_stats DESC limit 50";
    private static final String SQL_QUERY_INSERT = "INSERT INTO anonymizationphoto_stats ( id_stats, id_resource_anonymized, date_anonymization, elapsed_time_in_seconds, faces_detected ) VALUES ( ?, ?, ?, ?, ? ) ";

    /**
     * Generates a new primary key.
     *
     * @return The new primary key
     */
    private Long newPrimaryKey( )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK ) ; )
        {
            daoUtil.executeQuery( );
            Long nKey = null;

            if ( daoUtil.next( ) )
            {
                nKey = daoUtil.getLong( 1 );
            }

            return nKey;
        }
    }

    /**
     * Get list stats anonymization.
     *
     * @return liste stats
     */
    public List<ResponseAnonymizationPhoto> load( )
    {

        List<ResponseAnonymizationPhoto> listStats = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) ; )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                int nIndex = 1;
                ResponseAnonymizationPhoto stat = new ResponseAnonymizationPhoto( );
                stat.setdResource( daoUtil.getInt( nIndex++ ) );
                stat.setDateAnonymized( DateUtil.getDateString( daoUtil.getDate( nIndex++ ), Locale.FRENCH ) );
                stat.setElapsedTimeSeconde( daoUtil.getString( nIndex++ ) );
                stat.setNbFacesDetected( daoUtil.getInt( nIndex++ ) );

                listStats.add( stat );

            }
        }

        return listStats;
    }

    /**
     * Insert BDD an annymization stat.
     *
     * @param response
     *            response object content stat anonymization
     */
    public void insert( ResponseAnonymizationPhoto response )
    {

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) ; )
        {
            int nIndex = 1;
            daoUtil.setLong( nIndex++, newPrimaryKey( ) );
            daoUtil.setInt( nIndex++, response.getIdResource( ) );
            daoUtil.setDate( nIndex++, DateUtil.formatDateSql( DateUtil.getCurrentDateString( Locale.FRENCH ), Locale.FRENCH ) );
            daoUtil.setString( nIndex++, response.getElapsedTimeSeconde( ) );
            daoUtil.setInt( nIndex, response.getNbFacesDetected( ) );
            daoUtil.executeUpdate( );
        }
    }
}
