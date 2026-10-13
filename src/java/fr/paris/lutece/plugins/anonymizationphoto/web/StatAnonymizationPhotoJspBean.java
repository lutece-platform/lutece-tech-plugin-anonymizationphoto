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
package fr.paris.lutece.plugins.anonymizationphoto.web;

import fr.paris.lutece.plugins.anonymizationphoto.business.StatisticsHome;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

/**
 * Back office page showing the last photo anonymizations.
 */
@RequestScoped
@Named
@Controller( controllerJsp = "StatAnonymizationPhoto.jsp", controllerPath = "jsp/admin/plugins/anonymizationphoto/", right = StatAnonymizationPhotoJspBean.RIGHT_STAT_ANONYMIZATION, securityTokenEnabled = true )
public class StatAnonymizationPhotoJspBean extends MVCAdminJspBean
{

    private static final long serialVersionUID = 1L;

    // TEMPLATES
    private static final String TEMPLATE_STATS_ANONYMIZATION = "/admin/plugins/anonymizationphoto/stat_anonymization.html";

    // Right
    public static final String RIGHT_STAT_ANONYMIZATION = "STAT_ANONYMIZATION_MANAGEMENT";

    // Properties
    private static final String PROPERTY_PAGE_TITLE = "anonymizationphoto.adminFeature.stat.anonymizationphoto.name";

    // Views
    private static final String VIEW_STAT_ANONYMIZATION = "statAnonymizationPhoto";

    // MARK
    private static final String MARK_LIST_STATS_ANONYMIZATION = "listStatsAnonymization";

    /**
     * Displays the last anonymization statistics.
     *
     * @param model
     *            the model of the page
     * @return the page
     */
    @View( value = VIEW_STAT_ANONYMIZATION, defaultView = true )
    public String getStats( Models model )
    {
        model.put( MARK_LIST_STATS_ANONYMIZATION, StatisticsHome.loadStatsAnonymization( ) );

        return getPage( PROPERTY_PAGE_TITLE, TEMPLATE_STATS_ANONYMIZATION );
    }
}
