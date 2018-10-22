/*
Descripción de funcionalidad:
Todo el texto enviado por medio del monitor serial se imprimirá
en la pantalla LCD, y el contraste sera controlado por medio del
potenciometro.

  Circuito:
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
 */

// Incluir la libreria:
#include <LiquidCrystal.h>
#include <Keypad.h>
#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>


//Variables de teclado
const byte rowsLength = 4;
const byte colsLength = 4;
byte rowPin [rowsLength] = {12, 8, 7, 4};
byte colPin [colsLength] = {A1,A2,A3,A4};

char keys[rowsLength][colsLength] = {
  {'1', '2', '3' , 'A'},
  {'4', '5', '6' , 'B'},
  {'7', '8', '9' , 'C'},
  {'*', '0', '#' , 'D'}
};

Keypad kb = Keypad( 
  makeKeymap( keys ), 
  rowPin,
  colPin,
  rowsLength,
  colsLength
);
char keyPressed;

//Creación de variables de display LCD
String mensaje="";
// Inicializar la libreria con el numero de los pines
LiquidCrystal lcd(11, 10, 9, 6, 5, 3);

// Definimos el pin digital donde se conecta el sensor de humedad
#define DHTPIN 2
// Dependiendo del tipo de sensor de humedad
#define DHTTYPE DHT11
// Definimos el pin analógico donde se conecta la fotoresistencia
#define LDRPin A0

// Declaramos constantes necesarias para calcular la luminosidad
const long A = 1000; //Resistencia en oscuridad en KΩ
const int B = 15; //Resistencia a la luz (10 Lux) en KΩ
const int Rc = 10; //Resistencia calibracion en KΩ
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
}

void loop() {
  keyPressed = kb.getKey();
  if( keyPressed != NO_KEY ){
    Serial.println( keyPressed );
  }else{
    
  
  
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
  // Mostramos la humedad obtenida por el sensor
  //Serial.println(mensajeH);
  // Creamos el mensaje que vamos a imprimir para la temperatura
  String mensajeT = "Temperatura ";
  mensajeT += t;
  mensajeT += " gradosC ";
  // Mostramos la temperatura obtenida por el sensor
  //Serial.println(mensajeT);

  // ----------LUMINOSIDAD----------
  // Leemos el voltaje producido por la fotoresistencia
  V = analogRead(LDRPin);
  // Calculamos la luminosidad en base al cálculo anterior
  iluminacion = ((long)V*A*10)/((long)B*Rc*(1024-V));
  // Creamos el mensaje que vamos a imprimir para la iluminación
  String mensajeI = "Iluminacion ";
  mensajeI += iluminacion;
  mensajeI += " lumens";
  // Mostramos la iluminación obtenida por la fotoresistencia
  //Serial.println(mensajeI);

  String mensajeFinal = mensajeH+mensajeT+mensajeI;
  Serial.println(mensajeFinal);
  delay(100);
  }
   
  //Verificar si se tiene información pendiente por revisar

  if(Serial.available()){
    delay(100);
    //Limpiar la pantalla LCD
    //input = Serial.read();
    //mensaje += input;
    while(Serial.available()>0){
      mensaje = Serial.readStringUntil('\n');
      //Lectura de caracteres   
      imprimirMensaje(mensaje);
      //mensaje="";
    }
  }else{
    //Serial.println(mensaje);
      //imprimirMensaje(mensaje);
    }
}

void imprimirMensaje(String msj){
  int contLinea=0;
  int bla=(msj.length()/16)+1;
  String mensaje [bla];
  for(int i=1;i<msj.length()+1;i++){
    mensaje[contLinea]+=msj.charAt(i-1);
    if(i%16==0){
      contLinea++;
    }
  }
  for(int i=1;i<bla;i++){
    lcd.clear();
    //Serial.println(mensaje[i]);
    lcd.setCursor(0, 0);
    lcd.print(mensaje[i-1]);
  
    lcd.setCursor(0, 1);
    lcd.print(mensaje[i]);
    delay(2500);
    
  }
}

