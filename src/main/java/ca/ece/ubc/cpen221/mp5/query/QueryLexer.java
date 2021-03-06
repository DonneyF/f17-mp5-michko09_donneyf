// Generated from Query.g4 by ANTLR 4.7
package ca.ece.ubc.cpen221.mp5.query;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class QueryLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, OR=6, AND=7, GT=8, GTE=9, LT=10, 
		LTE=11, EQ=12, NUM=13, LPAREN=14, RPAREN=15, STRING=16, WS=17;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "OR", "AND", "GT", "GTE", "LT", 
		"LTE", "EQ", "NUM", "LPAREN", "RPAREN", "STRING", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'in'", "'category'", "'name'", "'rating'", "'price'", "'||'", "'&&'", 
		"'>'", "'>='", "'<'", "'<='", "'='", null, "'('", "')'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, "OR", "AND", "GT", "GTE", "LT", "LTE", 
		"EQ", "NUM", "LPAREN", "RPAREN", "STRING", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public QueryLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Query.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\23\u009d\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4"+
		"\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3"+
		"\b\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\16\3\16"+
		"\3\16\3\16\5\16Z\n\16\5\16\\\n\16\3\17\3\17\3\20\3\20\3\21\6\21c\n\21"+
		"\r\21\16\21d\3\21\3\21\6\21i\n\21\r\21\16\21j\3\21\3\21\3\21\3\21\6\21"+
		"q\n\21\r\21\16\21r\3\21\3\21\6\21w\n\21\r\21\16\21x\3\21\3\21\3\21\3\21"+
		"\6\21\177\n\21\r\21\16\21\u0080\3\21\3\21\3\21\6\21\u0086\n\21\r\21\16"+
		"\21\u0087\3\21\3\21\3\21\6\21\u008d\n\21\r\21\16\21\u008e\3\21\7\21\u0092"+
		"\n\21\f\21\16\21\u0095\13\21\3\22\6\22\u0098\n\22\r\22\16\22\u0099\3\22"+
		"\3\22\2\2\23\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16"+
		"\33\17\35\20\37\21!\22#\23\3\2\6\3\2\63\67\3\2\62;\7\2))\60\60\62;C\\"+
		"c|\5\2\13\f\17\17\"\"\2\u00ad\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2"+
		"\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\3%\3\2\2\2\5(\3\2\2\2\7\61\3\2\2\2\t"+
		"\66\3\2\2\2\13=\3\2\2\2\rC\3\2\2\2\17F\3\2\2\2\21I\3\2\2\2\23K\3\2\2\2"+
		"\25N\3\2\2\2\27P\3\2\2\2\31S\3\2\2\2\33[\3\2\2\2\35]\3\2\2\2\37_\3\2\2"+
		"\2!b\3\2\2\2#\u0097\3\2\2\2%&\7k\2\2&\'\7p\2\2\'\4\3\2\2\2()\7e\2\2)*"+
		"\7c\2\2*+\7v\2\2+,\7g\2\2,-\7i\2\2-.\7q\2\2./\7t\2\2/\60\7{\2\2\60\6\3"+
		"\2\2\2\61\62\7p\2\2\62\63\7c\2\2\63\64\7o\2\2\64\65\7g\2\2\65\b\3\2\2"+
		"\2\66\67\7t\2\2\678\7c\2\289\7v\2\29:\7k\2\2:;\7p\2\2;<\7i\2\2<\n\3\2"+
		"\2\2=>\7r\2\2>?\7t\2\2?@\7k\2\2@A\7e\2\2AB\7g\2\2B\f\3\2\2\2CD\7~\2\2"+
		"DE\7~\2\2E\16\3\2\2\2FG\7(\2\2GH\7(\2\2H\20\3\2\2\2IJ\7@\2\2J\22\3\2\2"+
		"\2KL\7@\2\2LM\7?\2\2M\24\3\2\2\2NO\7>\2\2O\26\3\2\2\2PQ\7>\2\2QR\7?\2"+
		"\2R\30\3\2\2\2ST\7?\2\2T\32\3\2\2\2U\\\t\2\2\2VY\t\2\2\2WX\7\60\2\2XZ"+
		"\t\3\2\2YW\3\2\2\2YZ\3\2\2\2Z\\\3\2\2\2[U\3\2\2\2[V\3\2\2\2\\\34\3\2\2"+
		"\2]^\7*\2\2^\36\3\2\2\2_`\7+\2\2` \3\2\2\2ac\t\4\2\2ba\3\2\2\2cd\3\2\2"+
		"\2db\3\2\2\2de\3\2\2\2e\u0093\3\2\2\2fh\7\"\2\2gi\t\4\2\2hg\3\2\2\2ij"+
		"\3\2\2\2jh\3\2\2\2jk\3\2\2\2k\u0092\3\2\2\2lm\7\"\2\2mn\7(\2\2np\7\"\2"+
		"\2oq\t\4\2\2po\3\2\2\2qr\3\2\2\2rp\3\2\2\2rs\3\2\2\2s\u0092\3\2\2\2tv"+
		"\7/\2\2uw\t\4\2\2vu\3\2\2\2wx\3\2\2\2xv\3\2\2\2xy\3\2\2\2y\u0092\3\2\2"+
		"\2z{\7\"\2\2{|\7/\2\2|~\7\"\2\2}\177\t\4\2\2~}\3\2\2\2\177\u0080\3\2\2"+
		"\2\u0080~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\u0092\3\2\2\2\u0082\u0092"+
		"\7\u00eb\2\2\u0083\u0085\7\61\2\2\u0084\u0086\t\4\2\2\u0085\u0084\3\2"+
		"\2\2\u0086\u0087\3\2\2\2\u0087\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088"+
		"\u0092\3\2\2\2\u0089\u008a\7\"\2\2\u008a\u008c\7*\2\2\u008b\u008d\t\4"+
		"\2\2\u008c\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008c\3\2\2\2\u008e"+
		"\u008f\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0092\7+\2\2\u0091f\3\2\2\2\u0091"+
		"l\3\2\2\2\u0091t\3\2\2\2\u0091z\3\2\2\2\u0091\u0082\3\2\2\2\u0091\u0083"+
		"\3\2\2\2\u0091\u0089\3\2\2\2\u0092\u0095\3\2\2\2\u0093\u0091\3\2\2\2\u0093"+
		"\u0094\3\2\2\2\u0094\"\3\2\2\2\u0095\u0093\3\2\2\2\u0096\u0098\t\5\2\2"+
		"\u0097\u0096\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u0097\3\2\2\2\u0099\u009a"+
		"\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u009c\b\22\2\2\u009c$\3\2\2\2\17\2"+
		"Y[djrx\u0080\u0087\u008e\u0091\u0093\u0099\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}