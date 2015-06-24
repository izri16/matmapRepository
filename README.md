matmapRepository
================
Funkcionalita aplikácie: 

1. Tlačidlo Search v hlavnom menu (vľavo).
   Po kliknutí zobrazí užívateľovi lokalitu, v ktorej sa nachádza alebo alternatívnu správu pokiaľ žiadnu nenašlo
2. Tlačidlo New location v hlavnom menu (v strede).
  Po kliknutí sa užívateľovi zobrazí okno čakajúce na vstup do lokality, do ktorej chce násť cestu. Po tom čo užívateľ začne písať, mu zobrazuje názvy vyhovujúce jeho vstupu (pokiaľ také existujú). Následne užívateľa lokalizuje a zobrazí mu body cez, ktoré sa dostane na požadované miesto (pokiaľ taká trasa existuje).
3. Tlačidlo History v hlavnom menu (vpravo).
 Zobrazí históriu posledných hľadaní do určitej lokality. Ak je hľadaní pre nejaké miesto viacej, zobrazí len najnovší. Po kliknutí na nejaký zo záznamov sa znovu spustí vyhľadávanie. V bočnom menu panely sa nachádza tlačidlo delete a po jeho odkliknutí sa užívateľovi zobrazí okno umožňujúce mazať záznamy.
4. Tlačidlo Record creator v hlavnom menu (skryté, zobrazí sa po kliknutí na bočný panel v menu).
  Užívateľ dostaneme možnosť zadávať mená pre vlastne body. Tieto sa pridávajú do Manažéra záznamov(Record manager), do ktorého sa dá dostať po kliknutí na ikonu pera v hornom panely. V bočnom menu sa tiež nachádza možnosť pridať záznamy do textového súboru, ktorá vytvorí súbor v priečinku Downloads kde záznamy uloží. (Za bežných okolností by sa použil priečinok Documents, ktorý je vhodejší, avšak nie je podporovaný všetkými verziamy systému Android)

 Manažér záznamov (Record manager)
 
 Zobrazí sa zoznam všetkých záznamov. V menu sú možnosti Zmazať a Vyhľadať podľa mena. V časi zmazať je v skrytom menu ešte možnosť 'Vyčistiť históriu a susedov', ktorá zmaže z týchto miest riadky, ktoré boli už vymazané zo záznamov. V prípade, že užívaťeľ zvolí možnosť vymazať všetky záznamy sa tieto vymažú automaticky. 
 
 Po kliknutí na konkrétny záznam sa zobrazia údaje o všetkých wifinách, ktoré boli pre daný záznam namerané. V tejto časti nájde v menu užívateľ možnosť vytvoriť susedov, čo je spôsob ako prepojiť body aby sa v nich dala hľadať cesta. Rovnako je tu možnosť premenovať daný záznam a aplikovať zmeny buď na jeden konkrétny alebo všetky záznamy z daným názvom.
 
 Pokiaľ užívateľ klikne na jednu z wifín zobrazia sa mu o nej kompletné informácie.
 
Čo sa plánovalo a nestihlo:
 
Trasu sme pôvodne plánovali vizualizovať nielen vypísať, avšak vytvorenie užitočného vytvárača záznamov (Record creator) zabralo značnú časť času a jeho úloha sa ukázala byť celkom kľúčová. Druhým dôvodom bola pomerne zložitá práca z obrázkami, ktoré sa nezmestia do pamäte a snaha urobiť lokalizáciu čo najviac všeobecnú bez viazania sa na konkrétne miesto.

UI je značne obmedzené z dôvodu vývoja pod pomerne starým zariadením, ktoré nepodporuje viaceré vlastnosti nových UI prvkov. Výhodou je však podpora na takmer ľubovolnom zariadení so systémom Android.
 

