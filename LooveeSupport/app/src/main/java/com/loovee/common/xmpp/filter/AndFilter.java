package com.loovee.common.xmpp.filter;

import com.loovee.common.xmpp.packet.Packet;

import java.util.ArrayList;
import java.util.List;

public class AndFilter implements PacketFilter {
	private List<PacketFilter> filters = new ArrayList<PacketFilter>();

	public AndFilter() {
		
	}

	public AndFilter(PacketFilter[] filters) {
		if (filters == null) {
			throw new IllegalArgumentException("Parameter cannot be null.");
		}
		for (PacketFilter filter : filters) {
			if (filter == null) {
				throw new IllegalArgumentException("Parameter cannot be null.");
			}
			this.filters.add(filter);
		}
	}

	public void addFilter(PacketFilter filter) {
		if (filter == null) {
			throw new IllegalArgumentException("Parameter cannot be null.");
		}
		this.filters.add(filter);
	}

	@Override
	public boolean accept(Packet packet) {
		for (PacketFilter filter : this.filters) {
			if (!filter.accept(packet)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return this.filters.toString();
	}
}