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

import configuration.ApplicationTestContextWithMockSession;
import eionet.webq.dao.orm.KnownHost;
import eionet.webq.dto.KnownHostAuthenticationMethod;
import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationTestContextWithMockSession.class})
@Transactional
public class KnownHostsTest {

    @Autowired
    private KnownHosts knownHosts;
    @Autowired
    private SessionFactory sessionFactory;

    @Before
    public void before() throws Exception {
        sessionFactory.getCurrentSession().setFlushMode(FlushMode.ALWAYS);
    }

    @Test
    public void shouldSaveKnownHost() throws Exception {
        KnownHost knownHost = new KnownHost();
        knownHost.setHostURL("http://host.url");
        knownHost.setAuthenticationMethod(KnownHostAuthenticationMethod.REQUEST_PARAMETER);
        knownHost.setHostName("Host name");
        knownHost.setKey("api-key");
        knownHost.setTicket("api-ticket");

        int id = knownHosts.save(knownHost);
        assertTrue(id > 0);
    }
}
