package Controller;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileReader;
import java.io.FileWriter   ;
import java.io.BufferedReader;
import java.io.BufferedWriter;
/**
 * @author Juda Alector
 * @since October 19, 2018
 * @description Clase encargada de eliminar, salvar o detiar mensajes.
 */
public class Menssager {
    // En esta variable se guardarán cada uno de los mensajes
    private String [] messages; 
    private Date date;
    //Variable para obtener la fecha
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    //Variable para obtener la hora
    DateFormat hourFormat = new SimpleDateFormat("HH:mm");
            
    public int size; 
    
    public Menssager (){
        this.messages = new String [10];
        for(int i = 0, e = this.messages.length; i < e; i++){
            this.messages[ i ] = null;
        }
        this.size = 0;
        this.readData( );
        this.print();
    }
    
    /**
        Método que salva mensajes y devuelve un verdadero o falso sí no se guarda.
        El primer mensaje tendrá el estado de los sensores, los demás tendrá la fecha junto
        del mensaje.
     */
    public boolean save(String message){
        boolean saved = false;
        if( this.size == 0 ) {
            this.messages[ size++ ] = message;
            saved = true;
        } else if( this.size < this.messages.length ) {
            date = new Date();
            this.messages[ size++ ] = dateFormat.format(date)+" "+hourFormat.format(date) + message;
            saved = true;
        }
        return saved;
    }
    /**
        Ese método elimina menajes y devuelve verdadero sí es que lo ha podido hacer. También 
        recorre la posición de cada uno de los menajes.
     */
    public boolean delete(int index){
        boolean deleted = false;        
        if( index < this.size ){
            for(int i = index; i < this.size; i++ ) {
                if( i == this.messages.length ) this.messages[ this.messages.length ] = null;
                else this.messages[i] = this.messages[i+1];
            }            
            this.size--;
            deleted = true;
        }
        return deleted;
    }
    
    /**
        Ese método edita menajes y devuelve verdadero sí es que lo ha podido hacer.
     */
    public boolean edit(int index, String message){
        boolean edited = false;
        if( index < this.size ){
            this.messages[ index ] = message;
            edited = true;
        }
        return edited;
    }
    
    //Método que ayuda a depurar, sólo imprime los mensajes existentes.
    public void print(){
        System.out.println("***** Messages *****");
        for(int i = 0, e = this.size; i < e; i++){
            System.out.println("Message [ "+i+" ]: "+ this.messages[i]);
        }
    }
    //Devuelve un mensaje a partir de su índice
    public String get(int index){
        String result = null;
        if( index < this.size ){
            result = this.messages[ index ];
        }
        return result;
    }
	
    public static void main( String [] args ) {
        Menssager m = new Menssager();
        m.edit(0,"juda locote");
        m.edit(3, "juda está bien locote");
        m.delete(4);
        m.save( "Juda Alberto ");
	m.saveData();
    }
    
    private void readData() {
        try {
            FileReader file =  new FileReader("./file.txt");
            BufferedReader bufferedReader = new BufferedReader ( file );
            String line = null;
            
            while( (line = bufferedReader.readLine()) != null){
                this.save( line );
                System.out.println( line );
            }
            bufferedReader.close();
        }catch ( Exception e ) {
            e.printStackTrace();
        }
        
    }
    private void saveData(){
        try {
            FileWriter file = new FileWriter("./file.txt");
            BufferedWriter writer = new BufferedWriter( file );
            
            for (int i = 0, e = this.size; i < e; i++) {
                writer.write(this.messages[ i ]);
                writer.newLine();
            }            

            writer.close();
        }
        catch(Exception e) {
            e.printStackTrace( );
        }
    }
}
