package Vista;

import Controller.Menssager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import panamahitek.Arduino.PanamaHitek_Arduino;

public class Principal extends JFrame{
    PanamaHitek_Arduino arduino;
    Menssager m;
    private String [] messagesCombo; 
    public int size;
    JComboBox listaMensaje;
    JTextField areaMensajes;
    JButton btnAgregar, btnEliminar, btnModificar, btnGuardar, btnActSensores;
    JPanel pnlPrincipal;
    Principal(){
        super("Sistema mimetizador");
        crear();
        armar();
    }
    private void crear(){
        arduino = new PanamaHitek_Arduino();
        m = new Menssager();
        pnlPrincipal = new JPanel();
        pnlPrincipal.setLayout(null);
        ManejaBotones metodos = new ManejaBotones();
        pnlPrincipal.setBounds(0, 0, 800, 500);
        areaMensajes = new JTextField();
        areaMensajes.setBounds(50, 200, 660, 30);
        listaMensaje = new JComboBox();
        listaMensaje.setBounds(50, 300, 660, 30);
        btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(metodos);
        btnAgregar.setBounds(50, 400, 100, 30);
        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(metodos);
        btnEliminar.setBounds(170, 400, 100, 30);
        btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(metodos);
        btnModificar.setBounds(290, 400, 100, 30);
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(metodos);
        btnGuardar.setBounds(410, 400, 100, 30);
        btnActSensores = new JButton("Actualizar sensores");
        btnActSensores.addActionListener(metodos);
        btnActSensores.setBounds(530, 400, 180, 30);
    }
    private void armar(){
        pnlPrincipal.add(listaMensaje);
        pnlPrincipal.add(areaMensajes);
        pnlPrincipal.add(btnAgregar);
        pnlPrincipal.add(btnEliminar);
        pnlPrincipal.add(btnModificar);
        pnlPrincipal.add(btnGuardar);
        pnlPrincipal.add(btnActSensores);
        add(pnlPrincipal);
        try{
            arduino.arduinoTX("COM7",9600);
        }catch(Exception e){
            System.err.println("Errorsillo");
        }
    }
    public void lanzar(){
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setSize(800,500);
       setVisible(true);
    }
    //Clase manejadora de botónes
    public class ManejaBotones implements ActionListener{
        @Override
       public void actionPerformed(ActionEvent evento){
           if(evento.getSource()==btnAgregar){
                messagesCombo = m.save(areaMensajes.getText());
                actulizarLista();
           }else if(evento.getSource()==btnEliminar){
                messagesCombo = m.delete(listaMensaje.getSelectedIndex());
                actulizarLista();
           }else if(evento.getSource()==btnModificar){
                areaMensajes.setText((String) listaMensaje.getSelectedItem());
           }else if(evento.getSource()==btnGuardar){
                messagesCombo = m.edit(listaMensaje.getSelectedIndex(),areaMensajes.getText());
                actulizarLista();
           }else if(evento.getSource()==btnActSensores){
                
           }
       }
    }
    //Metodo actualizarLista, actualiza el JComboBox
    private void actulizarLista(){
        listaMensaje.removeAllItems();
        for (int i = 0; i < messagesCombo.length; i++) {
            if(messagesCombo[i] != null){
                listaMensaje.addItem(messagesCombo[i]);
            }
        }
        areaMensajes.setText("");
    }
    private void enviarInfoArduino(){
//z        try {
//            arduino.sendData(areaMensajes.getText());
//        } catch (Exception ex) {
//            
//        }
    }
    
    public static void main(String[] args) {
        Principal p = new Principal();
        p.lanzar();
    }
}
