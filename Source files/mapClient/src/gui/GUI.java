package gui;

//imports

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Classe che modella l'interfaccia grafica del client. Implementa l'interfaccia ActionListener per gestire gli input dell'utente.
 */
public class GUI implements ActionListener {
	
	private JFrame frame;
	private JPanel panel;
	private JButton connectionButton;
	private JButton dataButton;
	private JButton archiveButton;
	private JButton displayTreeButton;
	private JButton displayRulesButton;
	private JButton predictionButton;

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenuItem saveTreeMenuItem;
	private JMenuItem saveRulesMenuItem;
	private JMenuItem disconnectMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem guideMenuItem;

	private String ip;
	private String port;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private String answer;
	private String tableName;
	private String tree;
	private String treeRules;
	
	
	/**
	 * Costruttore della classe GUI.
	 * Richiama le routine di inizializzazione delle componenti dell'interfaccia grafica e inizializza alcuni parametri utili per la connessione al server.
	 * 
	 * @param args	IP e porta del server a cui connettersi
	 */
	public GUI(String args[]){
		
		buttonInit();
		menuBarInit();
		panelInit();
		menuBarAdds();
		buttonLinks();
		frameInit();
		
		ip=args[0];
		port=args[1];
		socket=null;
		out=null;
		in=null;

	}

	
	/**
	 * Inizializza i tasti del menu principale dell'interfaccia grafica.
	 */
	private void buttonInit() {
		connectionButton=new JButton("Connect to server");
		dataButton=new JButton("Learn Regression Tree from data");
		archiveButton=new JButton("Load Regression Tree from archive");
		displayTreeButton=new JButton("Display tree");
		displayRulesButton=new JButton("Display tree rules");
		predictionButton=new JButton("Make a prediction");
	}
	
	/**
	 * Inizializza i tasti della barra dei menu.
	 */
	private void menuBarInit() {
		menuBar=new JMenuBar();
		fileMenu=new JMenu("File");
		helpMenu=new JMenu("Help");
		saveTreeMenuItem=new JMenuItem("Save tree to .txt file");
		saveRulesMenuItem=new JMenuItem("Save rules to .txt file");
		disconnectMenuItem=new JMenuItem("Disconnect from current server");
		exitMenuItem=new JMenuItem("Exit");
		guideMenuItem=new JMenuItem("Open user guide");
	}
	
	/**
	 * Inizializza il pannello principale su cui saranno aggiunte le componenti dell'interfaccia grafica e ci aggiunge i tasti del menu principale.
	 */
	private void panelInit() {
		panel=new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panel.setLayout(new GridLayout(3, 2, 10, 10));
		panel.add(connectionButton);
		panel.add(dataButton);
		panel.add(archiveButton);
		panel.add(displayTreeButton);
		panel.add(displayRulesButton);
		panel.add(predictionButton);
	}
	
	/**
	 * Inizializza il frame principale su cui saranno aggiunte le componenti dell'interfaccia grafica e ci aggiunge pannello principale e barra dei menu.
	 */
	private void frameInit() {
		frame=new JFrame();
		ImageIcon logo=new ImageIcon("Files/tree.png");
		frame.setResizable(false);
		frame.setIconImage(logo.getImage());
		frame.add(panel, BorderLayout.CENTER);
		frame.add(menuBar, BorderLayout.NORTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Client");
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Aggiunge ad ogni tasto dell'interfaccia le azioni corrispondenti, li disattiva (tranne il tasto di connessione al server) e li rende privi della capacità di ricevere il focus.
	 */
	private void buttonLinks(){
		connectionButton.setFocusable(false);
		connectionButton.addActionListener(this);
		dataButton.setFocusable(false);
		dataButton.addActionListener(this);
		dataButton.setEnabled(false);
		archiveButton.setFocusable(false);
		archiveButton.addActionListener(this);
		archiveButton.setEnabled(false);
		displayTreeButton.setFocusable(false);
		displayTreeButton.addActionListener(this);
		displayTreeButton.setEnabled(false);
		displayRulesButton.setFocusable(false);
		displayRulesButton.addActionListener(this);
		displayRulesButton.setEnabled(false);
		predictionButton.setFocusable(false);
		predictionButton.addActionListener(this);
		predictionButton.setEnabled(false);
		saveTreeMenuItem.addActionListener(this);
		saveTreeMenuItem.setEnabled(false);
		saveRulesMenuItem.addActionListener(this);
		saveRulesMenuItem.setEnabled(false);
		disconnectMenuItem.addActionListener(this);
		disconnectMenuItem.setEnabled(false);
		exitMenuItem.addActionListener(this);
		guideMenuItem.addActionListener(this);
	}
	
	/**
	 * Aggiunge alla barra dei menu i suoi tasti.
	 */
	private void menuBarAdds() {
		fileMenu.add(saveTreeMenuItem);
		fileMenu.add(saveRulesMenuItem);
		fileMenu.add(disconnectMenuItem);
		fileMenu.add(exitMenuItem);
		helpMenu.add(guideMenuItem);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
	}
	
	/**
	 * Specifica le azioni associate ad ogni tasto presente nell'interfaccia.
	 * connectionButton connette al server.
	 * dataButton carica un albero da un database.
	 * archiveButton carica di un albero da un file su disco.
	 * displayTreeButton mostra a schermo l'albero caricato.
	 * displayRulesButton mostra a schermo le regole dell'albero caricato.
	 * predictionButton permette di attraversare l'albero in base alle scelte dell'utente ed ottenere il valore previsto.
	 * saveTreeMenuItem salva su un file di testo l'albero caricato.
	 * saveRulesMenuItem salva su un file di testo le regole dell'albero caricato.
	 * disconnectMenuItem disconnette dalla sessione corrente.
	 * guideMenuItem apre la guida utente del programma.
	 * exitMenuItem chiude il client.
	 * 
	 * @param event		il click dell'utente su un tasto dell'interfaccia grafica.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		

		if(event.getSource()==connectionButton) {

			try {
				@SuppressWarnings("unused")
				InetAddress addr = InetAddress.getByName(ip);
			} catch (UnknownHostException e) {
				System.out.println(e.toString());
			return;
			}

			try {
				socket = new Socket(ip, Integer.valueOf(port));	
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());	 // stream con richieste del client
			
			} catch (IOException e) {
				System.out.println(e.toString());
				return;
			}
			
			JOptionPane.showMessageDialog(null, "Successfully connected on "+ip+":"+port);
			
			dataButton.setEnabled(true);
			archiveButton.setEnabled(true);
			disconnectMenuItem.setEnabled(true);
		}
			
		else if(event.getSource()==dataButton){
			try {

				tableName = JOptionPane.showInputDialog("Enter database table name:");
				if(tableName==null)
					return;
			
				out.writeObject(0);
				out.writeObject(tableName);
				answer=in.readObject().toString();
				if(!answer.equals("OK")){
					JOptionPane.showMessageDialog(null, answer+"\nPlease reconnect.");
					dataButton.setEnabled(false);
					archiveButton.setEnabled(false);
					return;
				}
				
				out.writeObject(1);
			
				treeRules=(String)in.readObject();
				tree=(String)in.readObject();
			
				displayTreeButton.setEnabled(true);
				displayRulesButton.setEnabled(true);
				predictionButton.setEnabled(true);
				saveTreeMenuItem.setEnabled(true);
				saveRulesMenuItem.setEnabled(true);
				dataButton.setEnabled(false);
				archiveButton.setEnabled(false);
		
			}catch(IOException | ClassNotFoundException e){
				JOptionPane.showMessageDialog(null, "Error!\nThe specified table doesn't exist.\nPlease reconnect.");
				dataButton.setEnabled(false);
				archiveButton.setEnabled(false);
				return;
			}
		}
		
		else if(event.getSource()==archiveButton){
			try {
			
				tableName = JOptionPane.showInputDialog("Enter file name:");
				if(tableName==null)
					return;
			
				out.writeObject(2);
				out.writeObject(tableName);

				treeRules=(String)in.readObject();
				tree=(String)in.readObject();
			
				displayTreeButton.setEnabled(true);
				displayRulesButton.setEnabled(true);
				predictionButton.setEnabled(true);
				saveTreeMenuItem.setEnabled(true);
				saveRulesMenuItem.setEnabled(true);
				dataButton.setEnabled(false);
				archiveButton.setEnabled(false);
	
			}catch(IOException | ClassNotFoundException e){
				JOptionPane.showMessageDialog(null, "Error!\nThe specified file doesn't exist.\nPlease reconnect.");
				dataButton.setEnabled(false);
				archiveButton.setEnabled(false);
				return;
			}
			
		}
		
		else if(event.getSource()==displayTreeButton) {
			JTextArea textArea=new JTextArea(30, 70);
			textArea.setText(tree);
			textArea.setEditable(false);
			JScrollPane scrollPane=new JScrollPane(textArea);
			JOptionPane.showMessageDialog(null, scrollPane, "Loaded Regression Tree info", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else if(event.getSource()==displayRulesButton) {
			JTextArea textArea=new JTextArea(30, 70);
			textArea.setText(treeRules);
			textArea.setEditable(false);
			JScrollPane scrollPane=new JScrollPane(textArea);
			JOptionPane.showMessageDialog(null, scrollPane, "Loaded Regression Tree rules", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else if(event.getSource()==predictionButton) {
			char risp='N';
			String query="";
			try {
				
				do{
					out.writeObject(3);
					answer=in.readObject().toString();
				
					while(answer.equals("QUERY")){

						int path=0;
						query=in.readObject().toString();
						
						if(query.equals("OK"))
							break;
					
						JPanel pathPanel=new JPanel();
						pathPanel.add(new JTextArea(query, 10, 20));
						String[] options=new String[countLines(query)-1];
						for(int i=0; i<countLines(query)-1; i++) {
							options[i]=Integer.toString(i+1);
						}

						path=JOptionPane.showOptionDialog(null, pathPanel, "Select tree path", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					
						out.writeObject(path);
						answer=in.readObject().toString();
					}
				
					answer=in.readObject().toString();
					
					int choice=JOptionPane.showConfirmDialog(null, "Predicted class: "+answer+"\n\nWould you repeat?", "Predicted class", JOptionPane.YES_NO_OPTION);
					if(choice==JOptionPane.YES_OPTION) {
						risp='Y';
						out.writeObject('Y');
					}
					else {
						risp='N';
					}
					
				}while (Character.toUpperCase(risp)=='Y');
			
				socket.close();
				System.exit(0);
				}catch(IOException | ClassNotFoundException e){
					System.out.println(e.toString());
				}
		}
		
		else if(event.getSource()==saveTreeMenuItem) {
			JFileChooser fileChooser=new JFileChooser();
			fileChooser.setDialogTitle("Choose save directory");
			if(fileChooser.showSaveDialog(panel)==JFileChooser.APPROVE_OPTION) {
				File treeSave=fileChooser.getSelectedFile();
				PrintWriter out;
				try {
					out = new PrintWriter(treeSave+".txt");
					out.println(tree);
					out.close();
				} catch (FileNotFoundException e) {
					System.out.println(e.toString());	
				}			
			}
		}
		
		else if(event.getSource()==saveRulesMenuItem) {
			JFileChooser fileChooser=new JFileChooser();
			fileChooser.setDialogTitle("Choose save directory");
			if(fileChooser.showSaveDialog(panel)==JFileChooser.APPROVE_OPTION) {
				File treeSave=fileChooser.getSelectedFile();
				PrintWriter out;
				try {
					out = new PrintWriter(treeSave+".txt");
					out.println(treeRules);
					out.close();
				} catch (FileNotFoundException e) {
					System.out.println(e.toString());
				}			
			}
		}
		
		else if(event.getSource()==disconnectMenuItem) {
			try {
				dataButton.setEnabled(false);
				archiveButton.setEnabled(false);
				displayTreeButton.setEnabled(false);
				displayRulesButton.setEnabled(false);
				predictionButton.setEnabled(false);
				saveTreeMenuItem.setEnabled(false);
				saveRulesMenuItem.setEnabled(false);
				disconnectMenuItem.setEnabled(false);
				socket.close();
				
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
		
		else if(event.getSource()==guideMenuItem) {
			try {
				Desktop.getDesktop().open(new java.io.File("Files/userguide.pdf"));
			}catch(IOException e) {
				System.out.println(e.toString());
			}
		}
		
		else if(event.getSource()==exitMenuItem) {
			System.exit(0);
		}
	
	}
	
	/**
	 * Conta il numero di volte in cui si è andati a capo in una stringa.
	 * 
	 * @param s	la stringa su cui si vuole effettuare la conta.
	 * @return	un integer il cui valore è il numero di volte in cui si è andati a capo.
	 */
	private int countLines(String s) {
		return (s+" ").split("\r?\n").length;
	}
}
