import Enum.Piece;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Plan {

    List<Lieu> list_lieu = new ArrayList<>();
    List<Conexion> list_conexions;

    public Plan() {
        //la taille du plan dépend de config.nombre_lieu
        //le nombre de connexion de ConfigPartie.nombre_connexion
        //ConfigPartie.nombre_connexion
        //ConfigPartie.list_pieces_partie
        list_conexions = Conexion.getList_conexions_retenues();

        for (Piece piece : ConfigPartie.list_pieces_partie){
            List<Piece> list_pieces_connectes=new ArrayList<>();

            for (Conexion con : list_conexions){
                if(con.contains(piece)){
                    list_pieces_connectes.add(con.autre(piece));
                }
            }
            list_lieu.add(new Lieu(piece,list_pieces_connectes));
        }

    }

    public Lieu getRandomLieu(){
        return list_lieu.get(new Random().nextInt(list_lieu.size()));
    }

    public Lieu getLieuFromPiece(Piece piece){
        for (Lieu lieu : list_lieu){
            if (lieu.piece == piece) return lieu;

        }
        return null;
    }

    @Override
    public String toString() {
        String s="\n";
        for (Lieu l : list_lieu){
            s=s+l.toString()+"\n";
        }
        return s;
    }
    public void export(){
        BufferedImage fond_rosace = DrawTools.getImage(SmartLogic.repertoire+"image\\Rosace.png");
        for (Conexion con : list_conexions){
            if( (con.contains(Piece.A)) && (con.contains(Piece.B))) { DrawTools.drawLine(fond_rosace,600,215,680,275, Color.black);}
            if( (con.contains(Piece.A)) && (con.contains(Piece.C))) { DrawTools.drawLine(fond_rosace,530,285,700,585, Color.black);}
            if( (con.contains(Piece.A)) && (con.contains(Piece.D))) { DrawTools.drawLine(fond_rosace,490,280,490,720, Color.black);}
            if( (con.contains(Piece.A)) && (con.contains(Piece.E))) { DrawTools.drawLine(fond_rosace,265,555,440,285, Color.black);}
            if( (con.contains(Piece.B)) && (con.contains(Piece.C))) { DrawTools.drawLine(fond_rosace,785,455,785,550, Color.black);}
            if( (con.contains(Piece.B)) && (con.contains(Piece.D))) { DrawTools.drawLine(fond_rosace,695,415,535,725, Color.black);}
            if( (con.contains(Piece.B)) && (con.contains(Piece.E))) { DrawTools.drawLine(fond_rosace,680,390,300,610, Color.black);}
            if( (con.contains(Piece.C)) && (con.contains(Piece.D))) { DrawTools.drawLine(fond_rosace,680,735,595,780, Color.black);}
            if( (con.contains(Piece.C)) && (con.contains(Piece.E))) { DrawTools.drawLine(fond_rosace,320,645,665,645, Color.black);}
            if( (con.contains(Piece.D)) && (con.contains(Piece.E))) { DrawTools.drawLine(fond_rosace,300,730,385,785, Color.black);}
        }
        DrawTools.saveFile(fond_rosace,SmartLogic.repertoire+"export\\rosace.png");
    }
}
