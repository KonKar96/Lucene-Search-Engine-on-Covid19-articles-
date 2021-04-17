# Lucene Search Engine on Covid19 articles
ΚΑΡΑΠΑΤΗΣ ΚΩΝΣΤΑΝΤΙΝΟΣ - ΑΜ 2719

ΠΑΠΑΔΟΠΟΥΛΟΣ ΓΙΩΡΓΟΣ - ΑΜ 2904

![demo](https://github.com/KonKar96/Lucene-Search-Engine-on-Covid19-articles-/blob/main/ezgif.com-gif-maker.gif)

Αυτό είναι ένα demo (όχι σε πραγματικό χρόνο) του script σε python το οποίο κάνει το scrape από τις ακόλουθες σελίδες.

https://www.thelancet.com/coronavirus/collection?pageSize=100&startPage=&ContentItemCategory=Editorial

https://www.the-scientist.com/tag/covid-19

Αποθηκεύει πάνω από 500 ξεχωριστά άρθρα, δημιουργώντας ένα csv αρχείο που θα χρησιμοποιηθεί στο επόμενο στάδιο του project.

Το csv αρχείο περιέχει τους τίτλους και το περιεχόμενο των άρθρων χωρισμένα με το σύμβολο ("/-/").

Το python script χρησιμοποιεί την βιβλιοθήκη Selenium που χρησιμοποιείται για testing σελιδών,για αυτοματοποίηση,για scraping κτλ.

Χρησιμοποιεί τον chromedriver που θα πρέπει να είναι αντίστοιχη έκδοση με τον chrome του συστήματος που θα τρέξει το πρόγραμμα.

Έτρεξε σε Windows 10.

Εγκατάσταση του Selenium κάνουμε με την εντολή: pip install selenium

Οι αρχικές ρυθμίσεις του driver είναι να ορίσει την ανάλυση του παραθύρου, προσθέτει σαν extension το ublock ώστε να μην υπάρχουν διαφημίσεις που θα καθυστερήσουν το scrape.

Για την ταχύτητα του scrape έχουν μπει ρυθμίσεις στη μεταβλητή prefs οι οποίες "ξεγυμνώνουν" την σελίδα από περιττά στοιχεία με σκοπό την απόδοση.

![image](https://user-images.githubusercontent.com/25888398/115105946-90d28e80-9f6a-11eb-9d97-3eb6fd4d5bec.png)

Το πρόγραμμα σε πρώτο στάδιο κάνει scrape τα hrefs από τα άρθρα που περιέχουν τα links.
![image](https://user-images.githubusercontent.com/25888398/115105965-a5af2200-9f6a-11eb-91d3-6a1516020b23.png)

Στη συνέχεια ανοίγει κάθε link ξεχωριστά και βρίσκει-αποθηκεύει τον τίτλο και το περιεχόμενο και το προσθέτει σε ενα dictionary με key τον τίτλο και value το περιεχόμενο
![image](https://user-images.githubusercontent.com/25888398/115105971-aba50300-9f6a-11eb-9c4c-e6a18ac91e24.png)

Την συγκεκριμένη διαδικασία την κάνει 2 φορές με τις αντίστοιχες αλλαγές για τις 2 διαφορετικές σελίδες.

Τέλος τα προσθέτει στο csv αρχείο επαναληπτικά

Σε επόμενο στάδιο θα φορτώσουμε στο Java πρόγραμμα το csv αρχείο που δημιουργήσαμε και θα σχεδιάσουμε το ευρετήριο μας

Επαναληπτικά θα φορτώσουμε στη lucene τους τίτλους και το περιεχόμενο από τα άρθρα.

Το πλάνο για τη μηχανή αναζήτησης είναι να μπορεί να βρει εύκολα με keywords που θα δίνει ο χρήστης, σχετικούς τίτλους που θα εμφανίζονται με βάση την σχετικότητα τους.

Αντίστοιχα θα γίνει και για το περιεχόμενο των άρθρων.
