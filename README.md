Tema 1 - PA

Task 1 - Feribot

Trebuie sa obtin costul minim astfel incat sa ocup toate feriboturile. Astfel,
caut cu binary search costul minim in intervalul celei mai grele masini si suma
tuturor greutatilor. Verific daca pot distribui masinile in k feriboturi, adica
adaug masini intr-un feribot, cat timp nu depaseste costul maxim (adica mid-ul
care e actualizat la fiecare pas), calculand costul in timp ce parcurg vectorul.
Daca depaseste costul, inseamna ca trebuie sa adaug masina pe un nou feribot.
Daca masinile pot fi redistribuite, trebuie sa micesc intervalul, altfel il
maresc spre dreapta. La final intorc costul minim, care va fi salvat in
heaviest.
Complexitate temporala: O(nlogn)
Complexitate spatila: O(n)

Task 4 - Walsh

M-am inspirat din problema ZParcurgere. Voi stoca elementele cautate intr-un
vector. Salvez coordonatele x si y, apoi in result[i] voi returna elementul
cautat prin functia findElem. Aceasta functie are un caz de baza, matricea
1x1, iar daca negCadr este false, inseamna ca ma aflu in cele 3 cadrane normale,
daca este true, inseamna ca ma aflu in cadranul 4 si trebuie sa neg elementele.
Impart recursiv matricea in jumatati egale, apoi verific in ce cadran se afla
perechea mea. In functie de x si y, ma pot afla fie in cadranul 1, fie in
cadranul 2, fie in cadranul 3, fie in cadranul 4. La final, voi obtine
elementul prin restrangerea unui cadran pana ajung la o matrice 1x1 care
este exact elementul meu cautat.
Complexitate temporala: O(k)
Complexitate spatila: O(n)

Task 5 - BadGPT
Pentru acest exercitiu trebuie sa numar in cate moduri se pot forma sirurile.
Initializez de la inceput cu 1, pentru ca exista cel putin 1 mod in care pot sa
formez sirul (atunci cand il desfasor). Apoi, parcurg vectorul meu de perechi
unde am retinut perechi de tipul (litera, nrAparitii). Verific daca apare
litera n sau u. Daca nu apare, atunci rezultatul nu se modifica, deoarece nu
mai exista alt mod prin care sa pot rescrie sirul. Altfel, trebuie sa calculez
in cate moduri pot forma alte siruri, de exemplu, daca am nnn, am urmatorele
variante mn sau nm, rezulta 2 moduri. Calculez acest lucru cu ajutorul lui
fibonacci si il inmultesc la rezultatul final. Deoarece fibonacci normal este
ineficient, am abordat alta solutie, cu ajutorul unei matrici, adica pornesc de
la termenul general si recursiv ajung la solutia finala care consta in aceasta
matrice ridicata la puterea n - 1 si inmultirea cu termenul initial [1, 0], iar
rezultatul este primul element (fib(n)), adica matrice[0][0]. Pentru a calcula
matrice^(n - 1), am abordat solutia de la problema exponentiere logaritmica.
Adica in loc sa inmultesc de fiecare data cu matrice, calculez matrice^n/2,
apoi inmultesc acest rezultat cu el insusi, astfel scad complexitatea. Fac asta
recursiv, iar daca n este impar, inmultesc cu matrice si obtin astfel
rezultatul dorit. De asemenea, deoarece am numere mari, am folosit, asa cum se
precizeaza in enunt, modulo.
Complexitate temporala: O(nlog n)
Complexitate spatila: O(n^2)

Task 7 - Crypto
Am considerat si cazul in care nu am niciun ?, iar pentru asta a trebuit sa
numar daca am vreun ? in K. Daca nu am niciun ?, numar ca in mod obisnuit. Am
cazul de baza pentru un sir vid, am un singur mod, iar pentru cazul general
verific daca am caracterele egale din S si K. Daca da, atunci fie pot sa folosesc
litera, fie nu, adica pot pastra subsirul format deja si sa ignor litera sau sa
adaug litera la substring. Daca literele nu se potrivesc, atunci o ignor. Pentru
cazul cu ?, parcurg K si S, verific daca K[i] este ?, atunci inlocuiesc cu toate
caracterele posibile din S si numar subsirurile, altfel am litera deja, nu mai
trebuie sa inlocuiesc. Pentru a numara, apelez functia replace cu caracterul
respectiv. Daca nu am depasit substringul S si caracterele coincid, atunci pot
forma substringul pana la pozitia j + 1, iar la final pot sa aleg sa ignor litera.
Complexitate temporala: O(N*L)
Complexitate spatila: O(N*L)