/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xalan" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, Lotus
 * Development Corporation., http://www.lotus.com.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.xpath.rwapi.impl.parser;

import org.apache.xpath.rwapi.expression.NodeTest;
import org.apache.xpath.rwapi.expression.StepExpr;
import org.apache.xpath.rwapi.impl.KindTestImpl;
import org.apache.xpath.rwapi.impl.StepExprImpl;


/**
 *
 */
public class Singletons extends SimpleNode
{
    /**
     * DotDot singleton
     */
    static final protected Singletons DOTDOT = new Singletons(XPathTreeConstants.JJTDOTDOT);

    /**
     * Slash singleton
     */
    static final protected Singletons SLASH = new Singletons(XPathTreeConstants.JJTSLASH);

    /**
    * At singleton
    */
    static final protected Singletons AT = new Singletons(XPathTreeConstants.JJTAT);

    /**
     * NodeTest singleton
     */
    static final protected Singletons NODETEST = new Singletons(XPathTreeConstants.JJTNODETEST);

    /**
     * Root singleton
     */
    static final protected Singletons ROOT = new Singletons(XPathTreeConstants.JJTROOT);

    /**
     * Root descendants singleton
     */
    static final protected Singletons ROOTDESCENDANT = new Singletons(XPathTreeConstants.JJTROOTDESCENDANTS);

    /**
     * Minus singleton
     */
    static final protected Singletons MINUS = new Singletons(XPathTreeConstants.JJTMINUS);

    /**
             * Plus singleton
             */
    static final protected Singletons PLUS = new Singletons(XPathTreeConstants.JJTPLUS);

    /**
     * SlashSlash singleton
     */
    static final public StepExprImpl SLASHSLASH;

    static
    {
        KindTestImpl kt = new KindTestImpl();
        kt.setKindTest(NodeTest.ANY_KIND_TEST);

        SLASHSLASH = new StepExprImpl(StepExpr.AXIS_DESCENDANT_OR_SELF, kt);
    }

    /**
     * Constructor for Singletons.
     * @param i
     */
    private Singletons(int i)
    {
        super(i);
    }

    /**
     * Constructor for Singletons.
     * @param p
     * @param i
     */
    private Singletons(XPath p, int i)
    {
        super(p, i);
    }
}