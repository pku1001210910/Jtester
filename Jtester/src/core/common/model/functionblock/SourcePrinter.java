/*
 * Copyright (C) 2007 Júlio Vilmar Gesser.
 * 
 * This file is part of Java 1.5 parser and Abstract Syntax Tree.
 *
 * Java 1.5 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.5 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.5 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 08/10/2006
 */
package core.common.model.functionblock;

import org.jdom.Attribute;
import org.jdom.Element;

/**
 * @author Julio Vilmar Gesser
 */
public final class SourcePrinter {

//	public static Document syntaxTreeDocument;
	
    private int level = 0;

    private boolean indented = false;

    private final StringBuilder buf = new StringBuilder();

    private boolean changed;
    
    public static Element parentElement = null;
    
    public int getLevel(){
    	return level;
    }
    
    
    public void setLevel(int level) {
    	this.level=level;
    }
    
    public void indent() {
        level++;
    }

    public void unindent() {
        level--;
    }


    private void makeIndent() {
        for (int i = 0; i < level; i++) {
            buf.append("    ");
        }
    }

    public void print(String arg) {
        if (!indented) {
            makeIndent();
            indented = true;
        }
        buf.append(arg);
        changed = true;
    }

    public void println(String arg, Element element) {
    	if(parentElement == null){
    	element.setAttribute(new Attribute("label", arg));
    	parentElement = element;
    	}
    	else if(parentElement != null){
    		System.out.println("notParentElement" + arg);
    		element.setAttribute("label", arg);
        	parentElement.addContent(element);
    	}
        print(arg);
        println();
        changed = true;
    }
    
    public void println(String arg) {
        print(arg);
        println();
        changed = true;
    }

    public void println() {
        buf.append("\r\n");
        indented = false;
    }

    public String getSource() {
        return buf.toString();
    }

    @Override
    public String toString() {
        return getSource();
    }

	public void Lock() {
		this.changed = false;
	}

	public boolean isChanged() {
		return changed;
	}
}