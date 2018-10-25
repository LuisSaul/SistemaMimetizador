package View;

import Controller.Menssager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.panamahitek.PanamaHitek_Arduino;
import com.panamahitek.ArduinoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.panamahitek.PanamaHitek_MultiMessage;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.UIManager;
import jssc.SerialPortEventListener;
import jssc.SerialPortEvent;
import jssc.SerialPortException;

public class Principal extends JFrame {
    //Declaración de variables para gestionar arduino
    PanamaHitek_Arduino arduino;
    PanamaHitek_MultiMessage multi;
    SerialPortEventListener listener;
    //Llamar a la clase Menssager
    Menssager messager;
    //Creación de componentes que se mostrarán en la vista
    JPanel pnlPrincipal;
    JLabel lblIconoJavaArduino;
    JLabel lblSisMime,lblSaul,lblRafa,lblJuda;
    JLabel lblMessageArea,lblSelectMessage;
    JComboBox messageList;
    JTextField txtMessagesArea;
    JButton btnAdd, btnDelete, btnUpdate, btnSave, btnActSensores;

    //Creación del constructor de la clase
    Principal() {
        super("Sistema mimetizador");
        lookAndFell();
        create();
        assemble();
    }/
    /Método lookAndFell, Sirve para darle un diseño más agradable a la aplicación
    public void lookAndFell(){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                javax.swing.UnsupportedLookAndFeelException ex) {  
        }        
    }

    //Método create, dónde se inicializan todas las variables
    private void create() {
        arduino = new PanamaHitek_Arduino();
        multi = new PanamaHitek_MultiMessage(1, arduino);
        listener = new SerialPortEventListener() {
            @Overridemen
            public void serialEvent(SerialPortEvent spe) {
                try {
                    if (multi.dataReceptionCompleted()) {
                        char letra = multi.getMessage(0).charAt(0);
                        //--------------------------------------------------------
                        // Estos print sopara ver que enviá el arduino
                        System.out.print(" Información recibida: ");
                        System.out.println(multi.getMessage(0));
                        //--------------------------------------------------------
                        //Evaluamos sí exite erro o está enviando el estado de los sensores
                        if ((letra == 'H') || (letra == 'E')) {
                            //Sí no exite algún mensaje lo pone en la posición 0.
                            if (messager.size == 0) {
                                messager.save(multi.getMessage(0));
                            } else {
                                //Sí exite algún mensaje sólo lo edita. 
                                messager.edit(0, multi.getMessage(0));
                            }
                            updateList();
                        } else (letra >= '0' && letra <= '9') {
                            // Sí es una letra entre el 0 o el 9 enviamos el mensaje que está guardado.
                            sendArduinoInfo( Integer.parseInt(multi.getMessage( 0 )));
                        }
                        //Limpiamos el buffer para que el serial pueda enviar y recibir mensajes. 
                        multi.flushBuffer();
                    }
                } catch (ArduinoException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SerialPortException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        /**
            Inicialización de objetos para la vista y controlar el funcionamiento
        */
        messager = new Menssager();
        pnlPrincipal = new JPanel();
        pnlPrincipal.setLayout(null);
        ManageButtons metodos = new ManageButtons();
        lblSisMime = new JLabel("Sistema mimetizador");
        lblSisMime.setFont(new Font("",1,25));
        lblSisMime.setBounds(50,20,280,60);
        lblSaul = new JLabel("Ornelas Pérez Luis Saul");
        lblSaul.setFont(new Font("",1,17));
        lblSaul.setBounds(70,90,200,50);
        lblRafa = new JLabel("Paniagua Soto Rafael");
        lblRafa.setFont(new Font("",1,17));
        lblRafa.setBounds(70,120,200,50);
        lblJuda = new JLabel("Vallejo Herrera Juda Alector");
        lblJuda.setFont(new Font("",1,17));
        lblJuda.setBounds(70,150,250,50);
        lblIconoJavaArduino = new JLabel(new ImageIcon("src/Images/AplicationIcon.png"));
        lblIconoJavaArduino.setBounds(400, 5, 230, 230);
        pnlPrincipal.setBounds(0, 0, 650, 530);
        lblMessageArea = new JLabel("Escribir mensaje");
        lblMessageArea.setFont(new Font("",1,13));
        lblMessageArea.setBounds(50, 230, 120, 30);
        txtMessagesArea = new JTextField();
        txtMessagesArea.setBounds(50, 260, 550, 30);
        lblSelectMessage = new JLabel("Seleccionar mensaje");
        lblSelectMessage.setFont(new Font("",1,13));
        lblSelectMessage.setBounds(50, 300, 160, 30);
        messageList = new JComboBox();
        messageList.setBounds(50, 330, 550, 30);
        btnAdd = new JButton("Agregar");
        btnAdd.addActionListener(metodos);
        btnAdd.setBounds(50, 430, 80, 30);
        btnDelete = new JButton("Eliminar");
        btnDelete.addActionListener(metodos);
        btnDelete.setBounds(150, 430, 80, 30);
        btnUpdate = new JButton("Modificar");
        btnUpdate.addActionListener(metodos);
        btnUpdate.setBounds(250, 430, 80, 30);
        btnSave = new JButton("Guardar");
        btnSave.addActionListener(metodos);
        btnSave.setBounds(350, 430, 80, 30);
        btnActSensores = new JButton("Actualizar sensores");
        btnActSensores.addActionListener(metodos);
        btnActSensores.setBounds(450, 430, 140, 30);
    }
    //Método assemble, dónde se agregan todos los componentes a la vista
    private void assemble() {
        pnlPrincipal.add(lblSisMime);
        pnlPrincipal.add(lblSaul);
        pnlPrincipal.add(lblRafa);
        pnlPrincipal.add(lblJuda);
        pnlPrincipal.add(lblMessageArea);
        pnlPrincipal.add(messageList);
        pnlPrincipal.add(lblSelectMessage);
        pnlPrincipal.add(txtMessagesArea);
        pnlPrincipal.add(btnAdd);
        pnlPrincipal.add(btnDelete);
        pnlPrincipal.add(btnUpdate);
        pnlPrincipal.add(btnSave);
        pnlPrincipal.add(btnActSensores);
        pnlPrincipal.add(lblIconoJavaArduino);
        add(pnlPrincipal);
        try {
            // Se define el puerto porel que se va a generar la comunicación.
            arduino.arduinoRXTX("dev/ttyUSB0", 9600, listener);
            //arduino.arduinoRXTX("/dev/ttyUSB0", 9600, listener);
        } catch (ArduinoException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Método launch, es el que lanza la aplicación
    public void launch() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 530);
        setVisible(true);
        this.setResizable(false);
    }

    //Clase manejadora de botónes
    public class ManageButtons implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evento) {
            if (evento.getSource() == btnAdd) {
                String text = txtMessagesArea.getText();

                if (text.length() > 0) {
                    messager.save(txtMessagesArea.getText());
                    //sendArduinoInfo();
                }
                updateList();
            } else if (evento.getSource() == btnDelete) {
                messager.delete(messageList.getSelectedIndex());
                updateList();
            } else if (evento.getSource() == btnUpdate) {
                txtMessagesArea.setText(messager.get(messageList.getSelectedIndex()));
            } else if (evento.getSource() == btnSave) {
                messager.edit(messageList.getSelectedIndex(), txtMessagesArea.getText());
                updateList();
            } else if (evento.getSource() == btnActSensores) {
                try {
                    arduino.sendData("act_sensor");
                } catch (ArduinoException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SerialPortException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    //Metodo updateList, actualiza el JComboBox
    private void updateList() {
        messageList.removeAllItems();
        for (int i = 0; i < messager.size; i++) {
            messageList.addItem(i + ".- " + messager.get(i));
        }
        txtMessagesArea.setText("");
    }
    /*Método sendArduinoInfo, manda los mensajes que se mostrarán 
        en el display de Arduino*/
    private void sendArduinoInfo(int index) {
        try {
            if (messager.size > index) {
                arduino.sendData(messager.get(index));
            } else {
                arduino.sendData("Message no found ");
            }
        } catch (Exception ex) {
            System.err.println("Error al tratar de enviar un mensaje");
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Principal p = new Principal();
        p.launch();
    }
}
