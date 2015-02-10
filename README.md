matmapRepository
================
Aktuálna funkcionalita aplikácie: 

1. Lokalizácia (v aplikácií tlačítko search) 

Po kliknutí na tlačítko sa používateľovi zobrazí jeho aktuálna lokalita v budove 
FMFI. V prípade, že sa používateľa nepodarí z doposiaľ zozbieraných údajov lokalizovať, 
zobrazí sa o tom používateľovi správa. Pokiaľ používateľ nemá zapnutú wifi, zobrazí 
sa mu vyskakovacie okno, ktoré ho na to upozorní. Aplikácia zbiera informácie 
z okolitých wifín a na základe modelu určuje aktuálnu polohu. 

2. Zadanie požadovanej lokality (v aplikácií tlačítko new location) 
Zobrazí input do ktorého používateľ zadáva požadovanú lokalitu. Input zobrazuje 
používateľovi list miestností podľa vstupu, ktorý je zadaný. Miestnosti sú načítavané 
z databázy (zatiaľ obsahuje niekoľko akváriek, M 217, F1, F1 109, F2, M 218, A, B, C). 
Po odkliknutí sa miestnosť zapíše do histórie a pokiaľ v nej už hľadaná miestnosť 
existuje, zaradí sa na prvé miesto a aktualizuje sa jej čas pridania. 
Používateľovi sa po kliknutí zobrazí vyskakovacie okno so správou o pridaní do histórie. 
K dispozícií je tlačítko help, kde sa po kliknutí 
zobrazí nápoveda o tom, ako správne zadávať hľadané miestnosti. 

3. História (v aplikácií tlačítko history) 
Zobrazí zoznam už niekedy hľadaných miestností od najnovšieho cieľa po najstarší. 
V bočnom panely sú k dispozícií tlačítka delete a deleteAll. Po stlačení delete 
sa k zoznamu pridajú radio buttony, pomocou ktorých používateľ určí, ktoré položky 
chce odstrániť. DeleteAll odstráni všetky položky. V prípade, že je história prázdna 
je o tom zobrazená správa. 

Čo ďalej: 

V druhom semestri plánujeme pridať do databázy komplexné informácie o signále 
v ostatných miestnostiach, implementovať algoritmus, ktorý nájde najkratšiu cestu 
do požadovanej lokality a následnú vizualizáciu tejto trasy.
