package chrysalis.network;

import chrysalis.Chrysalis;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModPacketHandler {

	private static final String PROTOCOL_VERSION = "1";
	
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		    new ResourceLocation(Chrysalis.MODID, "main"),
		    () -> PROTOCOL_VERSION,
		    PROTOCOL_VERSION::equals,
		    PROTOCOL_VERSION::equals
		);
	
	public static void registerMessages() {
		int id = 0;
		INSTANCE.<VanillaPacketMessage>registerMessage(id++, VanillaPacketMessage.class, VanillaPacketMessage::encode, VanillaPacketMessage::decode, VanillaPacketMessage::handle);
	}
}
