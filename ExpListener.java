// Generated from Exp.g4 by ANTLR 4.13.0
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ExpParser}.
 */
public interface ExpListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ExpParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(ExpParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(ExpParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExp(ExpParser.ExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExp(ExpParser.ExpContext ctx);
}