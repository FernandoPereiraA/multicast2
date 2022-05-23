//Creación de un grupo de Chat. Todos los componentes del grupo pueden escribir y reciben los mensajes de los otros componentes
//El proceso principal crea el SocketMulticast
//Crea el grupo Multicast
//Crea un datagrampacket para enviar mensajes
//Lanza un hilo encargado de recibir y mostrar los mensajes entrantes. 
//El hilo finaliza  cuando se cierra el socket desde el proceso principal



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class Mchat {
        
        public static void main(String[] args) throws IOException
        {
                String strin;        
                int port=10000;
                byte[] vacio = new byte[0];

                //InetAddress grupo = InetAddress.getByName("231.0.0.1");
                
                InetAddress mcastaddr = InetAddress.getByName("231.0.0.1");
                InetSocketAddress grupo = new InetSocketAddress(mcastaddr, 10000);
                NetworkInterface netIf = NetworkInterface.getByName("localhost");
                
                if (!mcastaddr.isMulticastAddress()) {
                         System.out.println( "La ip: " + mcastaddr + " no es una ip multicas de la Clase D");
                         System.exit(0);
                }

                try {                         
                        MulticastSocket s = new MulticastSocket(port);
                 
                        s.joinGroup(grupo,netIf);
                       
                        System.out.println("Reunión del Grupo Multicast: 231.0.0.1   Puerto:10000");

                        //creamos un datagrama vacio
                        DatagramPacket dp = new DatagramPacket(vacio, 0,mcastaddr, port);
                          
                        // ChatThread will handle the incoming Data and print it out to STDN output.
                        new ChatThread(s).start();

                        // Leemos desde el teclado y enviamos los datos al grupo.                       
                        BufferedReader entrada = new BufferedReader(
                                       new InputStreamReader(System.in));  


                        System.out.println("Escribe cualquier cosa y RETURN, o salir para finalizar");
                        for(;;)
                        {
                                System.out.print(">");    
                                //Read from the STDN input
                                strin=entrada.readLine();
                               
                                System.out.println("mensaje"+ strin + " Longitud: " + strin.length());
                       
                                if (strin.equals("salir")) break; 
                                //cargamos el mensaje y su longitud
                                dp.setData(strin.getBytes());
                                dp.setLength(strin.length());
                                s.send(dp);
                        }

                        System.out.println("Dejando el grupo...");
                        s.leaveGroup(grupo,netIf);
                        s.close();
                } catch (Exception err){
                          System.err.println("ERR: Can not join the group " + err);
                          err.printStackTrace();
                          System.exit(1);
                }
        }
      
}


