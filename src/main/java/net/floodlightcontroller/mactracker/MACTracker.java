package net.floodlightcontroller.mactracker;

import java.util.Collection;
import java.util.Map;

import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFType;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;

import net.floodlightcontroller.core.IFloodlightProviderService;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.Set;
import net.floodlightcontroller.packet.Ethernet;
import org.openflow.util.HexString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MACTracker implements IOFMessageListener, IFloodlightModule {
	protected IFloodlightProviderService floodlightProvider;
	protected Set macAddresses;
	protected static Logger logger;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		 return MACTracker.class.getSimpleName();
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public void init(FloodlightModuleContext context)
			throws FloodlightModuleException {
		floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
	    macAddresses = new ConcurrentSkipListSet<Long>();
	    logger = LoggerFactory.getLogger(MACTracker.class);
		// TODO Auto-generated method stub

	}

	@Override
	public void startUp(FloodlightModuleContext context)
			throws FloodlightModuleException {
		 floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
		// TODO Auto-generated method stub

	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(
			IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
		// TODO Auto-generated method stub
		 Ethernet eth =
	                IFloodlightProviderService.bcStore.get(cntx,
	                                            IFloodlightProviderService.CONTEXT_PI_PAYLOAD);

	        Long sourceMACHash = Ethernet.toLong(eth.getSourceMACAddress());
	        if (!macAddresses.contains(sourceMACHash)) {
	            macAddresses.add(sourceMACHash);
	            logger.info("MAC Address: {} seen on switch: {}",
	                    HexString.toHexString(sourceMACHash),
	                    sw.getId());
	        }
	        return Command.CONTINUE;
	}

}
