package smartphone.csandaz.evanjelickyspevnik;

import android.view.View;

import smartphone.csandaz.evanjelickyspevnik.R;

/**
 * Created by csandaz on 4.1.2016.
 */
public class SongBook {
    BookType selectedBook;

    BookType Spevnik;
    BookType Antifony;
    BookType Pasie;
    BookType Funebral;
    BookType Funebral_Antifony;
    BookType Predspevy;
    BookType Vecera_Panova;
    BookType Liturgia;
    BookType Info;

    public SongBook(){
        Spevnik = new BookType(1, R.array.song_titles, "Spevník", "", View.VISIBLE);
        Antifony = new BookType(2, R.array.antifony_titles, "Antifóny", "antifony_cast_", View.GONE);
        Pasie = new BookType(3, R.array.pasie_titles, "Pašie", "pasie_", View.GONE);
        Funebral = new BookType(4, R.array.funebral_titles, "Funebrál", "funebral_", View.VISIBLE);
        Funebral_Antifony = new BookType(5, R.array.funebral_antifony_titles, "Pohrebné antifóny", "funebral_antifony_", View.VISIBLE);
        Predspevy = new BookType(6, R.array.predspevy_titles, "Predspevy", "predspevy_", View.GONE);
        Vecera_Panova = new BookType(7, R.array.vecera_panova_titles, "Večera Pánova", "vecera_panova_", View.GONE);
        Liturgia = new BookType(8, R.array.liturgia_titles, "Ďalšia liturgia", "liturgia_", View.GONE);
        Info = new BookType(9, R.array.info_titles, "Informácie", "info_", View.GONE);

        selectedBook = Spevnik;
    }
}

class BookType {
    //Knihy, ktore spevnik obsahuje.

    String title;       //Nadpis, ktory sa zobrazi pri tejto aktivnej knihe
    String filePath;    //predpona, ktora sa ma pridat pred cislo suboru aby sme ziskali spravnu cestu na subor. Napriklad piaty subor z antifon ziskame ak vyskladame antifony_5.txt
    int alternateSongPicker_Visibility; //urcuje viditelnost alternativneho sposobu vyberu piesne pri tejto knihe

    int id;
    int selectedSong = 1;
    int songTitles;     //Zoznam poloziek, ktore sa maju zobrazit v dialogovom okne pre vyber piesne

    public BookType(int id, int songTitles, String bookTitle, String filePath, int alternateSongPicker_Visibility) {
        this.id = id;
        this.title = bookTitle;
        this.songTitles = songTitles;
        this.filePath = filePath;
        this.alternateSongPicker_Visibility = alternateSongPicker_Visibility;
    }

    public String getSongPath(int songNumber){
        return filePath + songNumber + ".txt";
    }
}
