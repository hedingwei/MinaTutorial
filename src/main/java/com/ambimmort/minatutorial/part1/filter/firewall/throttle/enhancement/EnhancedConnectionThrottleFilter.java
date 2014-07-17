/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.minatutorial.part1.filter.firewall.throttle.enhancement;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ¶¨Î¡
 */
public class EnhancedConnectionThrottleFilter extends IoFilterAdapter {

    private static final long DEFAULT_TIME = 1000;

    private static final int DEFAULT_MAXCONNECTION = Integer.MAX_VALUE;

    private long allowedInterval;

    private int maxConnection;

    private final Map<String, Long> clients;

    private final static Logger LOGGER = LoggerFactory.getLogger(EnhancedConnectionThrottleFilter.class);

    /**
     * Default constructor. Sets the wait time to 1 second
     */
    public EnhancedConnectionThrottleFilter() {
        this(DEFAULT_TIME, DEFAULT_MAXCONNECTION);
    }

    /**
     * Constructor that takes in a specified wait time.
     *
     * @param allowedInterval The number of milliseconds a client is allowed to
     * wait before making another successful connection
     *
     */
    public EnhancedConnectionThrottleFilter(long allowedInterval, int maxConnection) {
        this.allowedInterval = allowedInterval;
        this.maxConnection = maxConnection;
        clients = Collections.synchronizedMap(new HashMap<String, Long>());
    }

    /**
     * Sets the interval between connections from a client. This value is
     * measured in milliseconds.
     *
     * @param allowedInterval The number of milliseconds a client is allowed to
     * wait before making another successful connection
     */
    public void setAllowedInterval(long allowedInterval) {
        this.allowedInterval = allowedInterval;
    }

    /**
     * Method responsible for deciding if a connection is OK to continue
     *
     * @param session The new session that will be verified
     * @return True if the session meets the criteria, otherwise false
     */
    protected boolean isConnectionOk(IoSession session) {
        // added logic
        if (clients.size() > this.maxConnection) {
            return false;
        }

        SocketAddress remoteAddress = session.getRemoteAddress();
        if (remoteAddress instanceof InetSocketAddress) {
            InetSocketAddress addr = (InetSocketAddress) remoteAddress;
            long now = System.currentTimeMillis();

            if (clients.containsKey(addr.getAddress().getHostAddress())) {
                LOGGER.debug("This is not a new client");
                Long lastConnTime = clients.get(addr.getAddress().getHostAddress());

                clients.put(addr.getAddress().getHostAddress(), now);

                // if the interval between now and the last connection is
                // less than the allowed interval, return false
                if (now - lastConnTime < allowedInterval) {
                    LOGGER.warn("Session connection interval too short");
                    return false;
                }

                return true;
            }

            clients.put(addr.getAddress().getHostAddress(), now);
            return true;
        }

        return false;
    }

    @Override
    public void sessionCreated(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
        if (!isConnectionOk(session)) {
            LOGGER.warn("Connections coming in too fast; closing.");
            session.close(true);
        }
        nextFilter.sessionCreated(session);
    }
}
