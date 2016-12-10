package server.communication;

import javax.swing.*;

import data.storage.Document;
import data.storage.FileNode;
import data.storage.UserPass;
import gui.ClientGUI;

import java.awt.Font;
import java.io.*;
import java.net.*;
import java.util.*;

/*
 * A Process that can be started multiple times, each time creating a different
 * connection to a server thread. Cannot be started before the Server process.
 * Will boot up a gui to handle user input.
 */
public class Client {
	
	private static ClientGUI ui = null;		// 
	
	private static Object lockObject = null;
	
	public static class Connection extends Observable {
		private Socket socket;

		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		
        /** Create socket, and receiving thread */
        public Connection(String server, int port) throws IOException {
            socket = new Socket(server, port);
            
            Thread receivingThread = new Thread() {
                @Override
                public void run() {
                    try {
                    	
                    	oos = new ObjectOutputStream(socket.getOutputStream());
                    	oos.flush();
                        
                    	ois = new ObjectInputStream(socket.getInputStream());
                    	
                    	try {
                    		while(true) {
                    			
								Object obj = ois.readObject();
								
								if (obj instanceof Document){
									Document doc = (Document)obj;
									
									if (doc.preview()){
										String txt = "";
										for ( String s : doc.getDataModel().getArr())
											txt += s;
										
										ui.prevTxt().setText(txt);
						  				ui.prevTxt().setFont(new Font("Arial", Font.PLAIN, 10));
									}
									else {
										System.out.println("Displaying Document..." + doc.getName());
										ui.createEditor(doc);
									}
								}
								else if (obj instanceof Boolean) {
									Boolean acceptedLogin = (Boolean) obj;
									ui.getLogin().serverResponse(acceptedLogin);
//									ui.receiveLogin();
								}
								else if (obj instanceof JTree) {
									JTree tree = (JTree) obj;
									
									synchronized (lockObject) {
										while (ui == null) {
											try{ lockObject.wait(); }
											catch (Exception e) {}
										}
									}
									ui.setTree(tree);
								}
								else if (obj instanceof String) {
									String message = (String)obj;
									System.out.println("Recieved JDialog Message: " + message);
									JOptionPane.showMessageDialog(ui.getFrame(), message);
								}
                    		}
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
                    } catch (IOException ex) {
                        notifyObservers(ex);
                    }
                }
            };
            receivingThread.start();
        }
        
        /** Send a line of text */
        public void send(String text) {
        	try {
        		oos.writeObject((Object) text);
            } catch (IOException ex) {
                notifyObservers(ex);
            }
        }
        
        /** Send a line of text */
        public void send(UserPass up) {
        	try {
        		oos.writeObject((Object)up);
            } catch (IOException ex) {
                notifyObservers(ex);
            }
        }
        
        /** Send a line of text */
        public void send(Document doc) {
        	try {
        		oos.writeObject((Object)doc);
            } catch (IOException ex) {
                notifyObservers(ex);
            }
        }
        
        /** Send a line of text */
        public void send(FileNode fn) {
        	try {
        		oos.writeObject((Object)fn);
            } catch (IOException ex) {
                notifyObservers(ex);
            }
        }
        
        /** Close the socket */
        public void close() {
            try {
                socket.close();
            } catch (IOException ex) {
                notifyObservers(ex);
            }
        }
        
        public Socket getSocket() {
        	return socket;
        }
        
        public ObjectOutputStream getOOS(){
        	return oos;
        }
		
        public ObjectInputStream getOIS(){
        	return ois;
        }
        
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		String server = "localhost";
        int port = 4444;
        Connection con = null;
        lockObject = new Object();
        try {
            con = new Connection(server, port);
        } catch (IOException ex) {
            System.out.println("Cannot connect to " + server + ":" + port);
            ex.printStackTrace();
            System.exit(0);
        }
        synchronized  (lockObject) {
	        ui = new ClientGUI(con);
	        lockObject.notifyAll();
        }
		
	}
}