package informationretrieval.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import informationretrieval.engine.SearchEngine;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.awt.Color;

public class MainSearchWindow {

	private JFrame frame;
	private JTextField txtSearchField;
	private JPopupMenu popupMenuAutocomplete;
	private JButton btnSearch;
	private JLabel lblAdvancedSearch;
	private JPopupMenu popupMenuAdvancedSearch;
	private JCheckBox chckbxByTitle;
	private JCheckBox chckbxByContents;
	private Box verticalBox;
	private JButton btnNext;
	private SearchEngine searchEngine;
	private boolean[] fieldsToSearch = {true, true, true};
	private String[] autocopleteSuggestions = {};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainSearchWindow window = new MainSearchWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public MainSearchWindow() throws IOException {
		searchEngine = new SearchEngine();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtSearchField = new JTextField();
		txtSearchField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
					try {
						popupMenuAutocomplete.removeAll();
						popupMenuAutocomplete.setVisible(false);
						if (e.getMark() > 0) {
							for (String str : autocopleteSuggestions) {
								String[] searchText = txtSearchField.getText().split(" ");
								if (str.toLowerCase().startsWith(searchText[searchText.length-1].toLowerCase())) {
									JLabel suggestion = new JLabel(str);
									suggestion.addMouseListener(new MouseAdapter() {
										public void mouseClicked(MouseEvent e) {
											try {
												searchText[searchText.length-1] = suggestion.getText();
												txtSearchField.setText(String.join(" ", searchText));
												popupMenuAutocomplete.removeAll();
												popupMenuAutocomplete.setVisible(false);
											} catch (Exception e1) {
												e1.printStackTrace();
											}
										}
									});
									suggestion.setFont(new Font("Tahoma", Font.PLAIN, 14));
									popupMenuAutocomplete.add(suggestion);
								}
							}
							popupMenuAutocomplete.show(frame.getContentPane(), 50, 30);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					if (e.getMark() == 0) {
						popupMenuAutocomplete.removeAll();
						popupMenuAutocomplete.setVisible(false);
					}
					txtSearchField.requestFocusInWindow();
			}
		});
		txtSearchField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxByTitle.isSelected() || chckbxByContents.isSelected())
					fieldsToSearch = new boolean[]{chckbxByTitle.isSelected(), 
							chckbxByContents.isSelected()};
				else
					fieldsToSearch = new boolean[]{true, true};
				if (!txtSearchField.getText().equals("")) {
					ArrayList<String> results = null;
					try {
						results = searchEngine.search(txtSearchField.getText(), fieldsToSearch, false);
					} catch (IOException | ParseException | InvalidTokenOffsetsException e1) {
						e1.printStackTrace();
					}
					verticalBox.removeAll();
					for (String result : results) {
						JLabel lblTitle = new JLabel("<html>" + result);
						lblTitle.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent e) {
								if (e.getButton() == MouseEvent.BUTTON1)
									try {
										DocumentInfoWindow.NewScreen(searchEngine.getDocumentInfo(result.split(":")[0].split(">")[1]));
									} catch (IOException | InvalidTokenOffsetsException e1) {
										e1.printStackTrace();
									}
							}
						});
						lblTitle.setFont(lblTitle.getFont().deriveFont(12));
						verticalBox.add(lblTitle);
						verticalBox.add(Box.createVerticalStrut(10));
					}
					if (results.size() == 0) {
						JLabel lblNoResults = new JLabel("NO DOCUMENTS WERE FOUND!");
						lblNoResults.setFont(new Font("Tahoma", Font.ITALIC, 18));
						verticalBox.add(lblNoResults);
						btnNext.setVisible(false);
					} else {
						btnNext.setVisible(true);
					}
					verticalBox.setVisible(true);
					autocopleteSuggestions = searchEngine.updateAutocomplete().toArray(autocopleteSuggestions);
					frame.validate();
					frame.repaint();
				}
			}
		});
		txtSearchField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtSearchField.setBounds(50, 6, 300, 24);
		frame.getContentPane().add(txtSearchField);
		txtSearchField.setColumns(10);
		
		popupMenuAutocomplete = new JPopupMenu();
		popupMenuAutocomplete.setBackground(Color.WHITE);
		popupMenuAutocomplete.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.getContentPane().add(popupMenuAutocomplete);
		
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxByTitle.isSelected() || chckbxByContents.isSelected())
					fieldsToSearch = new boolean[]{chckbxByTitle.isSelected(), 
							chckbxByContents.isSelected()};
				else
					fieldsToSearch = new boolean[]{true, true};
				if (!txtSearchField.getText().equals("")) {
					ArrayList<String> results = null;
					try {
						results = searchEngine.search(txtSearchField.getText(), fieldsToSearch, false);
					} catch (IOException | ParseException | InvalidTokenOffsetsException e1) {
						e1.printStackTrace();
					}
					verticalBox.removeAll();
					for (String result : results) {
						JLabel lblTitle = new JLabel("<html>" + result);
						lblTitle.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent e) {
								if (e.getButton() == MouseEvent.BUTTON1)
									try {
										DocumentInfoWindow.NewScreen(searchEngine.getDocumentInfo(result.split(":")[0].split(">")[1]));
									} catch (IOException | InvalidTokenOffsetsException e1) {
										e1.printStackTrace();
									}
							}
						});
						lblTitle.setFont(lblTitle.getFont().deriveFont(12));
						verticalBox.add(lblTitle);
						verticalBox.add(Box.createVerticalStrut(10));
					}
					if (results.size() == 0) {
						JLabel lblNoResults = new JLabel("NO DOCUMENTS WERE FOUND!");
						lblNoResults.setFont(new Font("Tahoma", Font.ITALIC, 18));
						verticalBox.add(lblNoResults);
						btnNext.setVisible(false);
					} else {
						btnNext.setVisible(true);
					}
					verticalBox.setVisible(true);
					autocopleteSuggestions = searchEngine.updateAutocomplete().toArray(autocopleteSuggestions);
					frame.validate();
					frame.repaint();
				}
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSearch.setBounds(360, 6, 80, 24);
		frame.getContentPane().add(btnSearch);
		
		popupMenuAdvancedSearch = new JPopupMenu();
		popupMenuAdvancedSearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.getContentPane().add(popupMenuAdvancedSearch);
		
		chckbxByTitle = new JCheckBox("Search by title");
		chckbxByTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		popupMenuAdvancedSearch.add(chckbxByTitle);
		
		chckbxByContents = new JCheckBox("Search by contents");
		chckbxByContents.setFont(new Font("Tahoma", Font.PLAIN, 14));
		popupMenuAdvancedSearch.add(chckbxByContents);
		
		lblAdvancedSearch = new JLabel("Advanced Search");
		lblAdvancedSearch.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1)
					popupMenuAdvancedSearch.show(lblAdvancedSearch, lblAdvancedSearch.getX(), lblAdvancedSearch.getY());
			}
		});
		lblAdvancedSearch.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdvancedSearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAdvancedSearch.setBounds(60, 30, 150, 24);
		frame.getContentPane().add(lblAdvancedSearch);
		
		verticalBox = Box.createVerticalBox();
		verticalBox.setBounds(17, 70, 450, 420);
		verticalBox.setVisible(false);
		frame.getContentPane().add(verticalBox);
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> results = null;
				try {
					results = searchEngine.getNext();
				} catch (IOException | ParseException | InvalidTokenOffsetsException e1) {
					e1.printStackTrace();
				}
				verticalBox.removeAll();
				for (String result : results) {
					JLabel lblTitle = new JLabel("<html>" + result);
					lblTitle.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							if (e.getButton() == MouseEvent.BUTTON1)
								try {
									DocumentInfoWindow.NewScreen(searchEngine.getDocumentInfo(result.split(":")[0].split(">")[1]));
								} catch (IOException | InvalidTokenOffsetsException e1) {
									e1.printStackTrace();
								}
						}
					});
					lblTitle.setFont(lblTitle.getFont().deriveFont(12));
					verticalBox.add(lblTitle);
					verticalBox.add(Box.createVerticalStrut(10));
				}
				verticalBox.setVisible(true);
				btnNext.setVisible(true);
				frame.validate();
				frame.repaint();
			}
		});
		btnNext.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNext.setBounds(175, 500, 80, 24);
		btnNext.setVisible(false);
		frame.getContentPane().add(btnNext);
	}
}
