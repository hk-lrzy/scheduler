package com.hklrzy.scheduler.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created 2018/12/9.
 *
 * @author ke.hao
 */
public class NetUtils {

    private static final Logger LOG =
            LoggerFactory.getLogger(NetUtils.class);
    private static final String LOOP_BACK_ADDRESS = "127.0";
    private static final String LAN_ADDRESS = "192.168";


    public static String getLocalHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOG.error("Get the local hostname failed. return local ip instead.", e);
            return getLocalAddress();
        }
    }

    public static String getLocalAddress() {
        try {
            final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            final ArrayList<String> ipv4Result = new ArrayList<>();
            final ArrayList<String> ipv6Result = new ArrayList<>();
            while (interfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.getDisplayName().contains("docker")) {
                    continue;
                }

                final Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    final InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress()) {
                        if (address instanceof Inet6Address) {
                            ipv6Result.add(address.getHostAddress());
                        } else {
                            ipv4Result.add(address.getHostAddress());
                        }
                    }
                }
            }

            if (!ipv4Result.isEmpty()) {
                for (String ip : ipv4Result) {
                    if (ip.startsWith(LOOP_BACK_ADDRESS) || ip.startsWith(LAN_ADDRESS)) {
                        continue;
                    }

                    return ip;
                }

                return ipv4Result.get(ipv4Result.size() - 1);
            } else if (!ipv6Result.isEmpty()) {
                return ipv6Result.get(0);
            }

            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            LOG.error("get local address failed", e);
        }

        return null;
    }

    public static boolean isValid(String address) {
        final String[] hostAndPort = address.split(":");
        return isValid(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
    }

    public static boolean isValid(String host, int port) {
        try {
            new InetSocketAddress(host, port);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
