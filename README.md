# Lucene Search Engine on Covid19 articles
![demo](https://github.com/KonKar96/Lucene-Search-Engine-on-Covid19-articles-/blob/main/ezgif.com-gif-maker.gif)

Αυτό είναι ένα demo (όχι σε πραγματικό χρόνο) του script σε python το οποίο κάνει το scrape από τις ακόλουθες σελίδες.

https://www.thelancet.com/coronavirus/collection?pageSize=100&startPage=&ContentItemCategory=Editorial

https://www.the-scientist.com/tag/covid-19

Αποθηκεύει πάνω από 500 ξεχωριστά άρθρα, δημιουργόντας ένα csv αρχείο που θα χρησιμοποιηθεί στο επόμενο στάδιο του project.

Το csv αρχείο περιέχει τους τίτλους και το περιεχόμενο των άρθρων χωρισμένα με το σύμβολο ("/-/").

Το python script χρησιμοποιεί την βιβλιοθήκη Selenium που χρησιμοποιείται για testing σελιδών,για αυτοματοποίηση,για scraping κτλ.

Χρησιμοποιεί τον chromedriver που θα πρέπει να είναι αντίστοιχη έκδοση με τον chrome του συστήματος που θα τρέξει το πρόγραμμα.

Έτρεξε σε Windows 10.

Εγκατάσταση του Selenium κάνουμε με την εντολή.

pip install selenium


Σε επόμενο στάδιο θα φορτώσουμε στο Java πρόγραμμα το csv αρχείο που δημιουργήσαμε και θα σχεδιάσουμε το ευρετήριο μας

Επαναληπτικά θα φορτώσουμε στη lucene τους τίτλους και το περιεχόμενο από τα άρθρα.

Το πλάνο για τη μηχανή αναζήτησης είναι να μπορεί να βρει εύκολα με keywords που θα δίνει ο χρήστης, σχετικούς τίτλους που θα εμφανίζονται με βάση την σχετικότητα τους.

Αντίστοιχα θα γίνει και για το περιεχόμενο των άρθρων
