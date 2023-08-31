// Generated from Datatype.g4 by ANTLR 4.13.0
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DatatypeParser}.
 */
public interface DatatypeListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DatatypeParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(DatatypeParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatatypeParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(DatatypeParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatatypeParser#datatype}.
	 * @param ctx the parse tree
	 */
	void enterDatatype(DatatypeParser.DatatypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatatypeParser#datatype}.
	 * @param ctx the parse tree
	 */
	void exitDatatype(DatatypeParser.DatatypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatatypeParser#field}.
	 * @param ctx the parse tree
	 */
	void enterField(DatatypeParser.FieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatatypeParser#field}.
	 * @param ctx the parse tree
	 */
	void exitField(DatatypeParser.FieldContext ctx);
}