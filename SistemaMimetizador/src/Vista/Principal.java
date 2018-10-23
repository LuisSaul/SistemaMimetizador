package Vista;

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
import jssc.SerialPortEventListener;
import jssc.SerialPortEvent;
import jssc.SerialPortException;

public class Principal extends JFrame {

    PanamaHitek_Arduino arduino;
    PanamaHitek_MultiMessage multi;
    SerialPortEventListener listener;
    int numMessage = 0;
    Menssager m;
    private String[] messagesCombo;
    public int size;
    JComboBox listaMensaje;
    JTextField areaMensajes;
    JButton btnAgregar, btnEliminar, btnModificar, btnGuardar, btnActSensores;
    JPanel pnlPrincipal;

    Principal() {
        super("Sistema mimetizador");
        crear();
        armar();
    }

    private void crear() {
        messagesCombo = new String[9];
        arduino = new PanamaHitek_Arduino();
        multi = new PanamaHitek_MultiMessage(1, arduino);
        listener = new SerialPortEventListener() {
            @Override
            public void serialEvent(SerialPortEvent spe) {
                try {
                    if (multi.dataReceptionCompleted()) {
                        char letra = multi.getMessage(0).charAt(0);
                        if ((letra == 'H') || (letra == 'E')) {
                            messagesCombo[0] = multi.getMessage(0);
                            //System.out.println(multi.getMessage(0));
                            System.out.println(messagesCombo[0]);
                        } else {
                            if (letra >= '0' && letra <= '9') {
                                numMessage = Integer.parseInt(multi.getMessage(0));
                            }
                        }
                        multi.flushBuffer();
                    }
                } catch (ArduinoException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SerialPortException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        m = new Menssager();
        pnlPrincipal = new JPanel();
        pnlPrincipal.setLayout(null);
        ManejaBotones metodos = new ManejaBotones();
        pnlPrincipal.setBounds(0, 0, 650, 500);
        areaMensajes = new JTextField();
        areaMensajes.setBounds(50, 200, 550, 30);
        listaMensaje = new JComboBox();
        listaMensaje.setBounds(50, 300, 550, 30);
        btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(metodos);
        btnAgregar.setBounds(50, 400, 80, 30);
        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(metodos);
        btnEliminar.setBounds(150, 400, 80, 30);
        btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(metodos);
        btnModificar.setBounds(250, 400, 80, 30);
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(metodos);
        btnGuardar.setBounds(350, 400, 80, 30);
        btnActSensores = new JButton("Actualizar sensores");
        btnActSensores.addActionListener(metodos);
        btnActSensores.setBounds(450, 400, 160, 30);
    }

    private void armar() {
        pnlPrincipal.add(listaMensaje);
        pnlPrincipal.add(areaMensajes);
        pnlPrincipal.add(btnAgregar);
        pnlPrincipal.add(btnEliminar);
        pnlPrincipal.add(btnModificar);
        pnlPrincipal.add(btnGuardar);
        pnlPrincipal.add(btnActSensores);
        add(pnlPrincipal);
        try {
            arduino.arduinoRXTX("COM3", 9600, listener);
        } catch (ArduinoException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void lanzar() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 500);
        setVisible(true);
    }

    //Clase manejadora de botónes

    public class ManejaBotones implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent evento) {
            if (evento.getSource() == btnAgregar) {
                enviarInfoArduino();
                messagesCombo = m.save(areaMensajes.getText());
                actulizarLista();
            } else if (evento.getSource() == btnEliminar) {
                messagesCombo = m.delete(listaMensaje.getSelectedIndex());
                actulizarLista();
            } else if (evento.getSource() == btnModificar) {
                areaMensajes.setText((String) listaMensaje.getSelectedItem());
            } else if (evento.getSource() == btnGuardar) {
                messagesCombo = m.edit(listaMensaje.getSelectedIndex(), areaMensajes.getText());
                actulizarLista();
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

    //Metodo actualizarLista, actualiza el JComboBox

    private void actulizarLista() {
        listaMensaje.removeAllItems();
        for (int i = 0; i < messagesCombo.length; i++) {
            if (messagesCombo[i] != null) {
                listaMensaje.addItem(messagesCombo[i]);
            }
        }
        areaMensajes.setText("");
    }

    private void enviarInfoArduino() {
        try {
            String parteMensaje = "";
            for (int i = 1; i < messagesCombo[0].length() + 1; i++) {
                parteMensaje += messagesCombo[0].charAt(i - 1);
                if (i % 64 == 0) {
                    arduino.sendData(parteMensaje);
                    System.out.println(parteMensaje);
                    parteMensaje = "";
                } else if ((i == messagesCombo[0].length()) && (i % 64 != 0)) {
                    arduino.sendData(parteMensaje);
                    System.out.println(parteMensaje);
                }
            }
            //arduino.sendData(messagesCombo[0]);
        } catch (Exception ex) {

        }
    } 

    private void crearPrimerMensaje() {

    }

    public static void main(String[] args) {
        Principal p = new Principal();
        p.lanzar();
    }
}
