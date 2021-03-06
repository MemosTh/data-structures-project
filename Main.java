package javaPack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Main {
	public static File fl= null;
	public static BufferedReader data = null;
	public static int NumOfLines;
	public static List<String> datain = new ArrayList<String>();
	public static List<Hotel> hotel = new ArrayList<Hotel>();
        public static List<Hotel> hotelTRIE = new ArrayList<Hotel>();
	public static List<String> dataout = new ArrayList<String>();
	public static List<Trie> trie = new ArrayList<Trie>();
	public static Scanner scan;
	public static String path;
	public static int tofind;
	public static int lay = 0;
	public static int root = 0;
	public static int layers = 0;
	public static long time = 0;

	public static void main(String[] args) throws IOException {
		getFile();
		calData();
		assignData();
		bubblesort();
		menuDisp();
	}

	public static void getFile() throws FileNotFoundException {
		System.out.println("Δωσε την διευθυνση του αρχειου");
		scan = new Scanner(System.in);
		path = scan.next();
		fl = new File(path);
		if (!fl.exists()) {        //αν το αρχειο δεν υπαρχει το προγραμμα τερματιζει
			System.out.println("Η διεθυνση δεν ηταν σωστη το προγραμμα θα τερματησει\n");
			System.exit(0);
		} else { // αν υπαρχει φτιαχνει τον buffer
			System.out.println("Το αρχειο φορτωθηκε με επιτυχια");
			data = new BufferedReader(new FileReader(path));
		}
	}

	public static void calData() throws IOException {
		String temp = data.readLine(); // μετρα τα δεδομενα και τα αποθηκευει σε
                                               // ενδιαμεση λιστα για να μεταφερθουν
                                               // αργοτερα στην hotel
		char[] tempChar = temp.toCharArray();
		String outPut = "";
		for (int i = 0; i < tempChar.length; i++) {
			if (tempChar[i] != ';') {
				outPut = outPut + tempChar[i];
			} else {
				break;
			}
                           
		}
		NumOfLines = Integer.parseInt(outPut);
		System.out.println(NumOfLines + " Ξενοδοχεια υπαρχουν στο αρχειο");
		while ((temp = data.readLine()) != null) { 
			datain.add(temp + ";");
		}
	}

	public static void assignData() {
		for (int i = 0; i < NumOfLines; i++) {
			String tempSum = ""; // βαζει τα δεδομενα στα καταλληλα πεδια της
									// hotel
			hotel.add(new Hotel());
			hotelTRIE.add(new Hotel());
			int exCount = 0;
			int inexCount = 0;
			String temp = datain.get(i);
			char[] tempChar = temp.toCharArray();
			for (int j = 0; j < tempChar.length; j++) {
				if (tempChar[j] != ';') {
					tempSum = tempSum + tempChar[j];
				} else {
					if (exCount == 0) {
						exCount++;
						hotel.get(i).id = Integer.parseInt(tempSum);
						hotelTRIE.get(i).id = Integer.parseInt(tempSum);
						
					} else if (exCount == 1) {
						exCount++;
						hotel.get(i).name = tempSum;
						hotelTRIE.get(i).name = tempSum;
					} else if (exCount == 2) {
						exCount++;
						hotel.get(i).stars = Integer.parseInt(tempSum);
						hotelTRIE.get(i).stars = Integer.parseInt(tempSum);
					} else if (exCount == 3) {
						exCount++;
						hotel.get(i).numberOfRooms = Integer.parseInt(tempSum);
						hotelTRIE.get(i).numberOfRooms = Integer.parseInt(tempSum);
					} else {
						if (inexCount == 0) {
							inexCount++;
							hotel.get(i).res.add(new Res());
							hotelTRIE.get(i).res.add(new Res());
							hotel.get(i).res.get(hotel.get(i).res.size() - 1).name = tempSum;
							hotelTRIE.get(i).res.get(hotelTRIE.get(i).res.size() - 1).name = tempSum;
							addTrie(tempSum, i, 0);
						} else if (inexCount == 1) {
							inexCount++;
							hotel.get(i).res.get(hotel.get(i).res.size() - 1).checkinDate = tempSum;
							hotelTRIE.get(i).res.get(hotelTRIE.get(i).res.size() - 1).checkinDate = tempSum;
						} else if (inexCount == 2) {
							hotel.get(i).res.get(hotel.get(i).res.size() - 1).stayDurationDays = Integer
									.parseInt(tempSum);
							hotelTRIE.get(i).res.get(hotelTRIE.get(i).res.size() - 1).stayDurationDays = Integer
									.parseInt(tempSum);
							inexCount = 0;
						}
					}
					tempSum = "";
				}
			}
		}
	}


	public static void menuDisp() throws NumberFormatException, IOException {
		System.out.println("1. Load Hotels and Reservations from file");
		System.out.println("2. Save Hotels and Reservations to file");
		System.out.println("3. Add a Hotel (μαζί και τις κρατήσεις του)");
		System.out.println("4. Search and Display a Hotel by id");
		System.out.println("5. Display Reservations by surname search");
		System.out.println("6. Exit");
		int choise = scan.nextInt();
		if (choise == 1) {
			datain.clear(); // αδιαζει τις λιστες πριν απο τη φορτωση νεου
							// αρχειου
			hotel.clear();
			trie.clear();
			root = 0;
			lay = 0;
			layers = 0;
			getFile();
			calData();
			assignData();
			bubblesort();
			menuDisp();
		} else if (choise == 2) {
			makeOutPut();
			saveOutPut();
			dataout.clear();
			menuDisp();
		} else if (choise == 3) {
			addHotel();
			bubblesort();
			menuDisp();
		} else if (choise == 4) {
			System.out.println("1. Γραμμικη αναζητηση");
			System.out.println("2. Δυαδικη αναζητηση");
			System.out.println("3. Interpolation search");
			int sCh;
			sCh = scan.nextInt();
			if (sCh == 1) {
				dispHotelID();
				menuDisp();
			} else if (sCh == 2) {
				dispHotelIDBin();
				menuDisp();
			} else if (sCh == 3) {
				dispHotelIDInter();
				menuDisp();
			} else {
				menuDisp();
			}
		}  else if (choise == 5) {
			System.out.println("1. Γραμμικη αναζητηση");
			System.out.println("2. TRIE");
			int sCh;
			sCh = scan.nextInt();
			if (sCh == 1) {
				dispSur();
				menuDisp();
			} else if (sCh == 2) {
				dTrie();
				menuDisp();
			}

		} else {
			scan.close();
			makeOutPut();
			saveOutPut();
			System.exit(0);
		}
	}

	public static void dTrie() throws NumberFormatException, IOException {
		int flag = -1; // ψαχνει για τα στοιχεια που υπακουουν στις προυποθεσεις
						// των trie
		System.out.println("Δωσε το ονομα που θελεις να ψαξεις στο TRIE");
		String find = scan.next();
		for (int i = 0; i < trie.size(); i++) {
			if (find.charAt(0) == trie.get(i).name && trie.get(i).father == -1) {
				flag = i;
				break;
			}
		}
		if (flag == -1 || find.length() == 1) {
			System.out.println("Δεν υπαρχει τετοιος πελατης");
		} else {
			sTrie(find, 1, flag);
		}

	}

	public static void sTrie(String n, int i, int pre) throws NumberFormatException, IOException {
		int flag = -1; // ψαχνει αναδρομικα και προχωρα στο επομενο γραμμα
		for (int j = 0; j < trie.get(pre).ch.size(); j++) {
			if (n.charAt(i) == trie.get(trie.get(pre).ch.get(j)).name
					&& trie.get(trie.get(pre).ch.get(j)).father == pre) {
				flag = trie.get(pre).ch.get(j);
				break;
			}
		}
		if (flag == -1) {
			System.out.println("Δεν υπαρχει τετοιος πελατης");
		} else {
			if (i == n.length() - 1) {
				for (int j = 0; j < trie.get(flag).pos.size(); j++) {
					i = trie.get(flag).pos.get(j);
					String temp = (Integer.toString(hotelTRIE.get(i).id) + " " + hotelTRIE.get(i).name.toString() + " "
							+ Integer.toString(hotelTRIE.get(i).stars) + " " + hotel.get(i).numberOfRooms + " ");
					for (int x = 0; x < hotelTRIE.get(i).res.size(); x++) {
						temp = temp + hotelTRIE.get(i).res.get(x).name + " " + hotelTRIE.get(i).res.get(x).checkinDate
								+ " " + hotelTRIE.get(i).res.get(x).stayDurationDays + " ";
					}
					System.out.println(temp);
				}
			} else {
				i++;
				sTrie(n, i, flag);
			}
		}

	}

	public static void addTrie(String n, int pos, int i) {
		int flag = -1;// φτιαχνει η βρισκει τα δυο πρωτα γραμματα της λεξης
		int flag2 = -1;
		if (trie.size() == 0) {
			trie.add(new Trie(n.charAt(i), -1, -1));
			for (i = 1; i < n.length(); i++) {
				int father = trie.size() - 1;
				if (i == n.length() - 1) {
					trie.add(new Trie(n.charAt(i), pos, father));
				} else {
					trie.add(new Trie(n.charAt(i), -1, father));
				}
				trie.get(father).ch.add(trie.size() - 1);
			}
		} else {
			for (int j = 0; j < trie.size(); j++) {
				if (n.charAt(i) == trie.get(j).name && trie.get(j).father == -1) {
					flag = j;
					for (int x = 0; x < trie.get(j).ch.size(); x++) {
						if (trie.get(trie.get(j).ch.get(x)).name == n.charAt(i + 1)
								&& trie.get(trie.get(j).ch.get(x)).father == j) {
							flag2 = trie.get(j).ch.get(x);
							if (i == n.length() - 1) {
								trie.get(flag2).pos.add(pos);
							}
							break;
						}
					}
					break;
				}
			}
			i++;
			if (flag == -1) {
				if (i - 1 == n.length() - 1) {
					trie.add(new Trie(n.charAt(i - 1), pos, -1));
				} else {
					trie.add(new Trie(n.charAt(i - 1), -1, -1));
				}
				int father = trie.size() - 1;
				if (i == n.length() - 1) {
					trie.add(new Trie(n.charAt(i), pos, father));
				} else {
					trie.add(new Trie(n.charAt(i), -1, father));
				}
				trie.get(father).ch.add(trie.size() - 1);
				flag = 0;
				flag2 = trie.size() - 1;
			} else if (flag2 == -1) {
				if (i == n.length() - 1) {
					trie.add(new Trie(n.charAt(i), pos, flag));
				} else {
					trie.add(new Trie(n.charAt(i), -1, flag));
				}
				trie.get(flag).ch.add(trie.size() - 1);
				flag2 = trie.size() - 1;
			}
			i++;
			createTrie(n, pos, i, flag2);
		}

	}

	public static void createTrie(String n, int pos, int i, int flag) {
		if (i < n.length()) { // αναδρομικα φτιαχνει η βρισκει τα υπολοιπα
								// γραμματα της λεξης
			int flag2 = -1;
			for (int j = 0; j < trie.get(flag).ch.size(); j++) {
				if (trie.get(trie.get(flag).ch.get(j)).name == n.charAt(i)
						&& trie.get(trie.get(flag).ch.get(j)).father == flag) {
					flag2 = trie.get(flag).ch.get(j);
					if (i == n.length() - 1) {
						trie.get(flag2).pos.add(pos);
					}
				}
			}
			if (flag2 == -1) {
				if (i == n.length() - 1) {
					trie.add(new Trie(n.charAt(i), pos, flag));
				} else {
					trie.add(new Trie(n.charAt(i), -1, flag));
				}
				trie.get(flag).ch.add(trie.size() - 1);
				flag2 = trie.size() - 1;
			}
			flag = flag2;
			i++;
			createTrie(n, pos, i, flag);
		}
	}

	
	public static void dispHotelIDInter() {
		System.out.println("Δωσε το ID για interpolation search");
		Scanner idsc = new Scanner(System.in);
		tofind = idsc.nextInt();
		interpolation(0, hotel.size() - 1);
	}

	public static void interpolation(int from, int to) {
		int mid = (tofind - hotel.get(from).id) / (hotel.get(to).id - hotel.get(from).id) + from;
		if (from == to && hotel.get(to).id != tofind) {
			System.out.println("Δεν υπαρχει το στοιχειο με id " + tofind);
		} else {
			if (tofind > hotel.get(mid).id) {
				binary(mid + 1, to);
			} else if (tofind < hotel.get(mid).id) {
				binary(from, mid - 1);
			} else {
				int i = mid;
				String temp = (Integer.toString(hotel.get(i).id) + " " + hotel.get(i).name.toString() + " "
						+ Integer.toString(hotel.get(i).stars) + " " + hotel.get(i).numberOfRooms + " ");
				for (int x = 0; x < hotel.get(i).res.size(); x++) {
					temp = temp + hotel.get(i).res.get(x).name + " " + hotel.get(i).res.get(x).checkinDate + " "
							+ hotel.get(i).res.get(x).stayDurationDays + " ";
				}
				System.out.println(temp);
			}
		}
	}

	public static void dispHotelIDBin() {
		System.out.println("Δωσε το ID για binary search");
		Scanner idsc = new Scanner(System.in);
		tofind = idsc.nextInt();
		binary(0, hotel.size() - 1);
	}

	public static void binary(int from, int to) {
		int mid = (to - from) / 2 + from;
		if (from == mid && hotel.get(to).id != tofind) {
			System.out.println("Δεν υπαρχει το στοιχειο με id " + tofind);
		} else {
			if (tofind > hotel.get(mid).id) {
				binary(mid + 1, to);
			} else if (tofind < hotel.get(mid).id) {
				binary(from, mid - 1);
			} else {
				int i = mid;
				String temp = (Integer.toString(hotel.get(i).id) + " " + hotel.get(i).name.toString() + " "
						+ Integer.toString(hotel.get(i).stars) + " " + hotel.get(i).numberOfRooms + " ");
				for (int x = 0; x < hotel.get(i).res.size(); x++) {
					temp = temp + hotel.get(i).res.get(x).name + " " + hotel.get(i).res.get(x).checkinDate + " "
							+ hotel.get(i).res.get(x).stayDurationDays + " ";
				}
				System.out.println(temp);
			}
		}
	}

	public static void dispSur() {
		int dispFlag = 0;
                System.out.println("Δωσε το ονομα που θελεις να αναζητησεις");
		String tempString = scan.next();
		for (int i = 0; i < hotel.size(); i++) {
			for (int j = 0; j < hotel.get(i).res.size(); j++) {
				if (Objects.equals(tempString, hotel.get(i).res.get(j).name)) {
					String temp = (Integer.toString(hotel.get(i).id) + " " + hotel.get(i).name.toString() + " "
							+ Integer.toString(hotel.get(i).stars) + " " + hotel.get(i).numberOfRooms + " ");
					for (int x = 0; x < hotel.get(i).res.size(); x++) {
						temp = temp + hotel.get(i).res.get(x).name + " " + hotel.get(i).res.get(x).checkinDate + " "
								+ hotel.get(i).res.get(x).stayDurationDays + " ";
					}
					System.out.println(temp);
					dispFlag = 1;
				}
			}
		}
		if (dispFlag == 0) {
			System.out.println("Δεν υπαρχει ξενοδοχειο που να συμφωνει με τα κρητηρια αναζητησης");
		}
	}

	

	public static void dispHotelID() throws IOException {
		System.out.println("Δωσε το id του ξενοδοχειου που θελεις να δεις");
		int dispID = Integer.parseInt(scan.next());
		int dispFlag = 0;
		for (int i = 0; i < hotel.size(); i++) { // γραμμικη αναζητηση βαση
                                                        // του id
			if (hotel.get(i).id == dispID) { // αν το id της i εταιριας βρεθει
                                                        // εκτυπωνει ολη την εταιρια
				System.out.println("Το ξενοδοχειο με id " + dispID + " βρεθηκε");
				String temp = (Integer.toString(hotel.get(i).id) + " " + hotel.get(i).name.toString() + " "
						+ Integer.toString(hotel.get(i).stars) + " " + hotel.get(i).numberOfRooms + " ");
				for (int j = 0; j < hotel.get(i).res.size(); j++) {
					temp = temp + hotel.get(i).res.get(j).name + " " + hotel.get(i).res.get(j).checkinDate + " "
							+ hotel.get(i).res.get(j).stayDurationDays + " ";
				}
				System.out.println(temp);
				dispFlag = 1;
				break;
			}
		}
		if (dispFlag == 0) {
			System.out.println("Δεν υπαρχει ξενοδοχειο με id " + dispID);
		}
	}

	public static void bubblesort() {
		int flag = 0;
		for (int j = 0; j < hotel.size(); j++) {
			for (int i = 0; i < hotel.size() - 1 - j; i++) {
				if (hotel.get(i).id > hotel.get(i + 1).id) {
					int tempid = hotel.get(i).id;
					String temptitle = hotel.get(i).name;
					int tempstars = hotel.get(i).stars;
					int tempnum = hotel.get(i).numberOfRooms;
					List<Res> tempRes = new ArrayList<Res>(hotel.get(i).res);
					hotel.get(i).id = hotel.get(i + 1).id;
					hotel.get(i).name = hotel.get(i + 1).name;
					hotel.get(i).stars = hotel.get(i + 1).stars;
					hotel.get(i).numberOfRooms = hotel.get(i + 1).numberOfRooms;
					hotel.get(i).res.clear();
					hotel.get(i).res.addAll(hotel.get(i + 1).res);
					hotel.get(i + 1).id = tempid;
					hotel.get(i + 1).name = temptitle;
					hotel.get(i + 1).stars = tempstars;
					hotel.get(i + 1).numberOfRooms = tempnum;
					hotel.get(i + 1).res.clear();
					hotel.get(i + 1).res.addAll(tempRes);
					flag = 1;

				}
			}
			if (flag == 0) {
				break;
			}
			flag = 0;
		}
	}

	public static void addHotel() {
		boolean fFlag = true;
		int i = hotel.size();
		hotelTRIE.add(new Hotel());
		hotel.add(new Hotel());
		System.out.println("Δωσε το id του νεου ξενοδοχειου");
		hotel.get(i).id = -1;
		int temp = scan.nextInt();
		fFlag = checkID(temp);
		while (fFlag) {
			System.out.println("Το id αυτα υπαρχει παρακαλω δωστε αλλο");
			temp = scan.nextInt();
			fFlag = checkID(temp);

		}
		hotel.get(i).id = temp;
		hotelTRIE.get(i).id = hotel.get(i).id;
		System.out.println("Δωσε το ονομα του νεου ξενοδοχειου");
		Scanner nameSc = new Scanner(System.in);
		hotel.get(i).name = nameSc.nextLine();
		hotelTRIE.get(i).name = hotel.get(i).name;
		System.out.println("Δωσε τα αστερια του νεου ξενοδοχειου");
		hotel.get(i).stars = scan.nextInt();
		hotelTRIE.get(i).stars = hotel.get(i).stars;
		System.out.println("Δωσε των αριθμο των δωματιων του νεου ξενοδοχειου");
		hotel.get(i).numberOfRooms = scan.nextInt();
		hotelTRIE.get(i).numberOfRooms = hotel.get(i).numberOfRooms;
		int hasNext = 1;
		while (hasNext == 1) {
			hasNext = 0;
			System.out.println("Δωσε το ονομα της κρατησης");
			int j = hotel.get(i).res.size();
			hotel.get(i).res.add(new Res());
			hotelTRIE.get(i).res.add(new Res());
			Scanner nameSc2 = new Scanner(System.in);
			hotel.get(i).res.get(j).name = nameSc2.nextLine();
			hotelTRIE.get(i).res.get(j).name = hotel.get(i).res.get(j).name;
			addTrie(hotelTRIE.get(i).res.get(j).name, i, 0);
			System.out.println("Δωσε ημερομηνια κρατησης");
			Scanner nameSc3 = new Scanner(System.in);
			hotel.get(i).res.get(j).checkinDate = nameSc3.next();
			hotelTRIE.get(i).res.get(j).checkinDate = hotel.get(i).res.get(j).checkinDate;
			System.out.println("Δωσε μερες κρατησης");
			hotel.get(i).res.get(j).stayDurationDays = scan.nextInt();
			hotelTRIE.get(i).res.get(j).stayDurationDays = hotel.get(i).res.get(j).stayDurationDays;
			System.out.println("Υπαρχει αλλη κρατηση?\n1. Ναι\n2. Οχι");
			hasNext = scan.nextInt();
		}
		NumOfLines++;
        }
	public static boolean checkID(int x) {
		boolean found = false;
		for (int i = 0; i < hotel.size() - 1; i++) {
			if (x == hotel.get(i).id) {

				found = true;
			}
		}
		if (found) {
			return true;
		} else {
			return false;
		}

	}

	public static void makeOutPut() {
		int j = 0;
		for (int i = 0; i < hotel.size(); i++) {
			String temp;
			temp = hotel.get(i).id + ";" + hotel.get(i).name + ";" + hotel.get(i).stars + ";"
					+ hotel.get(i).numberOfRooms + ";";
			for (j = 0; j < hotel.get(i).res.size() - 1; j++) {
				temp = temp + hotel.get(i).res.get(j).name + ";" + hotel.get(i).res.get(j).checkinDate + ";"
						+ hotel.get(i).res.get(j).stayDurationDays + ";";
			}
			temp = temp + hotel.get(i).res.get(j).name + ";" + hotel.get(i).res.get(j).checkinDate + ";"
					+ hotel.get(i).res.get(j).stayDurationDays;
			dataout.add(temp);
		}
	}

	public static void saveOutPut() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new FileOutputStream(path));
		pw.write(Integer.toString(NumOfLines) + "\n");
		for (String str : dataout) {
			pw.write(str + "\n");
		}
		pw.close();
	}

}
