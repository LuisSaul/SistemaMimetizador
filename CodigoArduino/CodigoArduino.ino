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
char input;
String mensaje="";
// Inicializar la libreria con el numero de los pines
LiquidCrystal lcd(11, 10, 9, 6, 5, 3);

void setup() {
  // Configurar el tipo de LCD.
  lcd.begin(16, 2);
  // Configurar la comunicación serial.
  Serial.begin(9600);
}

void loop() {
  //Verificar si se tiene información pendiente por revisar
 // lcd.setCursor(5, 0);
  //lcd.print("ARDUINO!");
  
  //lcd.setCursor(3, 1);
  //lcd.print("LCD 16X2");

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
    Serial.println(mensaje[i]);
    lcd.setCursor(0, 0);
    lcd.print(mensaje[i-1]);
  
    lcd.setCursor(0, 1);
    lcd.print(mensaje[i]);
    delay(2500);
    
  }
}

