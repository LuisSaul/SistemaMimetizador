/*
  Conexion de display LCD:
 * LCD GND a tierra
 * LCD VCC pin a 5V
 * LCD V0 al potenciometro
 * LCD RS pin a pin digital 11
 * LCD RW a tierra
 * LCD Enable pin a pin digital 10
 * LCD D0 a tierra
 * LCD D1 a tierra
 * LCD D2 a tierra
 * LCD D3 a tierra
 * LCD D4 a pin digital 9
 * LCD D5 a pin digital 6
 * LCD D6 a pin digital 5
 * LCD D7 a pin digital 3
 * LCD R/W a tierra
 * LCD VSS a tierra
 * Resistencia  de 330 Ohms
 * Potenciometro 10 kOhms
 * 
  Conexión de teclado matricial:
 * Pin 1 a 12 digital de arduino
 * Pin 2 a 8 digital de arduino
 * Pin 3 a 7 digital de arduino
 * Pin 4 a 4 digital de arduino 
 * Pin 5 a A1 annalógico de arduino
 * Pin 6 a A2 annalógico de arduino
 * Pin 7 a A3 annalógico de arduino
 * Pin 8 a A4 annalógico de arduino
 * 
  Conexión de sensor de humedad:
 * Sensor de humedad pin central a pin digital 2 de arduino
 * Pin - a tierra
 * Pin + a vcc
 * 
  Conexión de foto-resistencia:
 * Conectada con una resistencia de 1 KOhm
 * y también al pin analogico A0
 * 
 * 
 * *** En Java se utiliza la librería Panama HiTek para 
 *     conectar arudino con Java. 
 *     
 * *** En arduino al utilizar la sentencia println se envian
 *     los datos impresos a la aplicación de Java. 
 */


// Incluir la libreria:
#include <LiquidCrystal.h>
#include <Keypad.h>
#include <Adafruit_Sensor.h>
#include <DHT.h>

//-----------Display LCD------------
String mensaje = "";
// Inicializar la libreria con el numero de los pines
LiquidCrystal lcd(11, 10, 9, 6, 5, 3);

//--------Teclado matricial--------
//Definimos el número de renglones del teclado matricial
const byte ROWSLENGTH = 4;
//Definimos el número de columnas del teclado matricial
const byte COLSLENGTH = 4;
//Definimos los pines de los renglones que van conectados a arduino
byte rowPin [ROWSLENGTH] = {12, 8, 7, 4};
//Definimos los pines de las columnas que van conectados a arduino
byte colPin [COLSLENGTH] = {A1,A2,A3,A4};

//Definimos la matriz de char con el nombre de los bótones del teclado
char keys[ROWSLENGTH][COLSLENGTH] = {
  {'1', '2', '3' , 'A'},
  {'4', '5', '6' , 'B'},
  {'7', '8', '9' , 'C'},
  {'*', '0', '#' , 'D'}
};
//Inicializamos el teclado
Keypad kb = Keypad( 
  makeKeymap( keys ), 
  rowPin,
  colPin,
  ROWSLENGTH,
  COLSLENGTH
);
//Definimos una variable de tipo char para que muestre cual tecla fue presionada
char keyPressed;

//----------Sensor de humedad y temperatura----------
// Definimos el pin digital donde se conecta el sensor de humedad
#define DHTPIN 2
// Dependiendo del tipo de sensor de humedad
#define DHTTYPE DHT11
// Definimos el pin analógico donde se conecta la fotoresistencia
#define LDRPin A0

// Declaramos constantes necesarias para calcular la luminosidad
const long A = 1000; //Resistencia en oscuridad en KΩ
const int B = 15; //Resistencia a la luz (10 Lux) en KΩ
const int RC = 10; //Resistencia calibracion en KΩ

//---------------Fotoresistencia--------------------
// Declaramos variables necesarias para calcular la luminosidad
int V = 0;
int iluminacion = 0;

// Inicializamos el sensor DHT11
DHT dht(DHTPIN, DHTTYPE);


void setup() {
  // Configurar el tipo de LCD.
  lcd.begin(16, 2);
  // Configurar la comunicación serial.
  Serial.begin(9600);
  // Comenzamos el sensor DHT
  dht.begin();
  //Enviamos por defecto la actualización de los sensores.
  sendSensorData();
}

void loop() {
  // Obtenemos el valor de el teclado
  keyPressed = kb.getKey();
  //Comprobamos sí una tecla fue presionada.
  if( keyPressed != NO_KEY ){
    // Enviamos la información de la tecla
    // La información es la contenida en keys
    Serial.println( keyPressed );
    //Sí el valor de esa tecla fue 'A'
    if( keyPressed == 'A'){
      //Enviamos el valor de los sensores.
      sendSensorData();
    }
  }
   
  //Verificar si se tiene información pendiente por revisar
  if(Serial.available()){
    //Fijar una espera para que el arduino lea toda la información del Serial
    delay(100);
    while(Serial.available() > 0){
      //Leer loa caracteres hasta un salto de linea y guardarlos en una variable
      mensaje = Serial.readStringUntil('\n');
      //Imprimir mensajes para visualizar en la intefaz de software que todo de esta enviando de forma correcta
      Serial.print("[Arduino UNO] recibido: ");
      //Se actualiza el sensor
      Serial.print( mensaje == "act_sensor");
      Serial.println(mensaje);
      /*Si se presiona el bóton de actualizar sensores en la interfaz de Java se envia la
        información que tienen almacenada los sensores*/
      if( mensaje == "act_sensor" ){
        //Llamada de metodo sendSensorData para mandar la nueva información sobre los sensores
        sendSensorData();
      }
      //De lo contrario se imprime el mensaje solicitado 
      else {
        //Lamada de metodo imprimirMensaje que recibe como parametro un String con el mensaje
        imprimirMensaje(mensaje);  
      }
    }
  }
}
//Creación de metodo imprimirMensaje
void imprimirMensaje(String msj){
  //Variabe para contar el número de linea en la que se esta guardando el mensaje
  int contLinea=0;
  //Definición de variable para definir el tamaño del arreglo
  int cantLinea=(msj.length()/16)+1;
  //Inicialización de variable mensaje
  String mensaje [cantLinea];
  //Ciclo para guardar los valores en las distintas posiciones del arreglo
  for(int i=1;i<msj.length()+1;i++){
    //Ir almacenando char por char en la posicion actual del arreglo "mensaje"
    mensaje[contLinea]+=msj.charAt(i-1);
    /*Si el residuo al dividir "i" entre 16 es igual a cero se incrementa el
      contador para agregar una nueva linea*/
    if(i%16==0){
      contLinea++;
    }
  }
  if( cantLinea == 1 ) {
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print(mensaje[0]);
    delay(1000);    
  } else {
    for(int i=1;i<cantLinea;i++){
      //Se limpia la información que contenga el display
      lcd.clear();
      //Se posiciona el cursor en el display para posteriormente imprimir el mensaje
      lcd.setCursor(0, 0);
      //Se imprime el mensaje en la parte superior del display
      lcd.print(mensaje[i-1]);
      //Se posiciona el cursor en el display para posteriormente imprimir el mensaje
      lcd.setCursor(0, 1);
      //Se imprime el mensaje en la parte inferior del display
      lcd.print(mensaje[i]);
      //Se da un tiempo de espera de 1000 milisegundos para poder leer el mensaje
      delay(1000);    
    }  
  }
}

void sendSensorData(){    
    // ----------HUMEDAD Y TEMPERATURA----------
    // Leemos la humedad relativa
    float h = dht.readHumidity();
    // Leemos la temperatura en grados centígrados (por defecto)
    float t = dht.readTemperature();
    // Comprobamos si ha habido algún error en la lectura
    if (isnan(h) || isnan(t)) {
      Serial.println("Error obteniendo los datos del sensor DHT11");
      return;
    }
    // Creamos el mensaje que vamos a imprimir para la humedad
    String mensajeH = "Humedad ";
    mensajeH += h;
    mensajeH += "% ";
    // Creamos el mensaje que vamos a imprimir para la temperatura
    String mensajeT = "Temperatura ";
    mensajeT += t;
    mensajeT += " gradosC ";
  
    // ----------LUMINOSIDAD----------
    // Leemos el voltaje producido por la fotoresistencia
    V = analogRead(LDRPin);
    // Calculamos la luminosidad en base al cálculo anterior
    iluminacion = ((long)V*A*10)/((long)B*RC*(1024-V));
    // Creamos el mensaje que vamos a imprimir para la iluminación
    String mensajeI = "Iluminacion ";
    mensajeI += iluminacion;
    mensajeI += " lumens";
    
    //Juntamos toda la información de los sensores para mandarla a arduino
    String mensajeFinal = mensajeH+mensajeT+mensajeI;
    Serial.println(mensajeFinal);
}
