package Controller;

/**
 *
 * @author Saul Ulises 
 */
public class Menssager {
    private String [] messages; 
    public int size; 
    
    public Menssager (){
        this.messages = new String [9];
        for(int i = 0, e = this.messages.length; i < e; i++){
            this.messages[ i ] = null;
        }
        this.size = 0;
    }
    
    public String [] save(String message){
        //boolean saved = false;
        if( this.size < this.messages.length ) {
            this.messages[ size++ ] = message;
            //saved = true;
        }
        return messages;
    }
    
    public String [] delete(int index){
        //boolean deleted = false;        
        if( index < this.size ){
            for(int i = index; this.messages[i+1] != null; i++ ) {
                this.messages[i] = this.messages[i+1];
            }
            this.messages[ size-1 ] = null;
            this.size--;
            //deleted = true;
        }
        
        return messages;
    }
    
    public String [] edit(int index, String message){
        //boolean edited = false;
        if( index < this.size ){
            this.messages[ index ] = message;
            //edited = true;
        }
        return messages;
    }
    
    public void print(){
        System.out.println("***** Messages *****");
        for(int i = 0, e = this.size; i < e; i++){
            System.out.println("Message [ "+i+" ]: "+ this.messages[i]);
        }
    }
    public String get(int index){
        String result = null;
        if( index < this.size ){
            result = this.messages[ index ];
        }
        return result;
    }
    
}