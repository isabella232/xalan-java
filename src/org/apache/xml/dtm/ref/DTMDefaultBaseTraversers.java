/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
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
package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.*;
import javax.xml.transform.Source;

import org.apache.xml.utils.XMLStringFactory;

/**
 * This class implements the traversers for DTMDefaultBase.
 */
public abstract class DTMDefaultBaseTraversers extends DTMDefaultBase
{

  /**
   * Construct a DTMDefaultBaseTraversers object from a DOM node.
   *
   * @param mgr The DTMManager who owns this DTM.
   * @param domSource the DOM source that this DTM will wrap.
   * @param source The object that is used to specify the construction source.
   * @param dtmIdentity The DTM identity ID for this DTM.
   * @param whiteSpaceFilter The white space filter for this DTM, which may
   *                         be null.
   * @param xstringfactory The factory to use for creating XMLStrings.
   */
  public DTMDefaultBaseTraversers(DTMManager mgr, Source source,
                                  int dtmIdentity,
                                  DTMWSFilter whiteSpaceFilter,
                                  XMLStringFactory xstringfactory)
  {
    super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory);
  }

  /**
   * This returns a stateless "traverser", that can navigate
   * over an XPath axis, though perhaps not in document order.
   *
   * @param axis One of Axes.ANCESTORORSELF, etc.
   *
   * @return A DTMAxisTraverser, or null if the given axis isn't supported.
   */
  public DTMAxisTraverser getAxisTraverser(final int axis)
  {

    DTMAxisTraverser traverser;

    if (null == m_traversers)  // Cache of stateless traversers for this DTM
    {
      m_traversers = new DTMAxisTraverser[Axis.names.length];
      traverser = null;
    }
    else
    {
      traverser = m_traversers[axis];  // Share/reuse existing traverser

      if (traverser != null)
        return traverser;
    }

    switch (axis)  // Generate new traverser
    {
    case Axis.ANCESTOR :
      traverser = new AncestorTraverser();
      break;
    case Axis.ANCESTORORSELF :
      traverser = new AncestorOrSelfTraverser();
      break;
    case Axis.ATTRIBUTE :
      traverser = new AttributeTraverser();
      break;
    case Axis.CHILD :
      traverser = new ChildTraverser();
      break;
    case Axis.DESCENDANT :
      traverser = new DescendantTraverser();
      break;
    case Axis.DESCENDANTORSELF :
      traverser = new DescendantTraverser();
      break;
    case Axis.FOLLOWING :
      traverser = new FollowingTraverser();
      break;
    case Axis.FOLLOWINGSIBLING :
      traverser = new FollowingSiblingTraverser();
      break;
    case Axis.NAMESPACE :
      traverser = new NamespaceTraverser();
      break;
    case Axis.NAMESPACEDECLS :
      traverser = new NamespaceDeclsTraverser();
      break;
    case Axis.PARENT :
      traverser = new ParentTraverser();
      break;
    case Axis.PRECEDING :
      traverser = new PrecedingTraverser();
      break;
    case Axis.PRECEDINGSIBLING :
      traverser = new PrecedingSiblingTraverser();
      break;
    case Axis.SELF :
      traverser = new SelfTraverser();
      break;
    case Axis.ALL :
      traverser = new AllTraverser();
      break;
    case Axis.ALLFROMNODE :
      traverser = new AllFromNodeTraverser();
      break;
    case Axis.DESCENDANTSFROMROOT :
      traverser = new AllFromRootTraverser();
      break;
    case Axis.ROOT :
      traverser = new RootTraverser();
      break;
    default :
      throw new DTMException("Unknown axis traversal type");
    }

    if (null == traverser)
      throw new DTMException("Axis traverser not supported: "
                             + Axis.names[axis]);

    m_traversers[axis] = traverser;

    return traverser;
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class AncestorTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node if this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {
      return m_parent[current & m_mask] | m_dtmIdent;
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {

      current = current & m_mask;

      while (DTM.NULL != (current = m_parent[current]))
      {
        if (m_exptype[current] == extendedTypeID)
          return current | m_dtmIdent;
      }

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class AncestorOrSelfTraverser extends AncestorTraverser
  {

    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  To see if
     * the self node should be processed, use this function.
     *
     * @param context The context node of this traversal.
     *
     * @return the first node in the traversal.
     */
    public int first(int context)
    {
      return context;
    }

    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  To see if
     * the self node should be processed, use this function.  If the context
     * node does not match the extended type ID, this function will return
     * false.
     *
     * @param context The context node of this traversal.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the first node in the traversal.
     */
    public int first(int context, int extendedTypeID)
    {
      return (m_exptype[context & m_mask] == extendedTypeID)
             ? context : next(context, context, extendedTypeID);
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class AttributeTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {
      return (context == current)
             ? getFirstAttribute(context) : getNextAttribute(current);
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {

      current = (context == current)
                ? getFirstAttribute(context) : getNextAttribute(current);

      do
      {
        if (m_exptype[current] == extendedTypeID)
          return current;
      }
      while (DTM.NULL != (current = getNextAttribute(current)));

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class ChildTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      // %OPT%
      return (context == current)
             ? getFirstChild(context) : getNextSibling(current);
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {

      current = (context == current)
                ? getFirstChild(context) : getNextSibling(current);

      do
      {
        if (m_exptype[current & m_mask] == extendedTypeID)
          return current;
      }
      while (DTM.NULL != (current = getNextSibling(current)));

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class DescendantTraverser extends DTMAxisTraverser
  {

    /**
     * Tell if this node identity is a descendant.  Assumes that
     * the node info for the element has already been obtained.
     *
     * %REVIEW% This is really parentFollowsRootInDocumentOrder ...
     * which fails if the parent starts after the root ends.
     * May be sufficient for this class's logic, but misleadingly named!
     *
     * NEEDSDOC @param subtreeRootIdentity
     * @param identity The index number of the node in question.
     * @return true if the index is a descendant of _startNode.
     */
    protected boolean isDescendant(int subtreeRootIdentity, int identity)
    {
      return _parent(identity) >= subtreeRootIdentity;
    }

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      int subtreeRootIdent = context & m_mask;

      for (current = (current & m_mask) + 1; ; current++)
      {
        int type = _type(current);  // may call nextNode()

        if (!isDescendant(subtreeRootIdent, current))
          return NULL;

        if (ATTRIBUTE_NODE == type || NAMESPACE_NODE == type)
          continue;

        return (current | m_dtmIdent);  // make handle.
      }
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {

      int subtreeRootIdent = context & m_mask;

      for (current = (current & m_mask) + 1; ; current++)
      {
        int exptype = _exptype(current);  // may call nextNode()

        if (!isDescendant(subtreeRootIdent, current))
          return NULL;

        if (exptype != extendedTypeID)
          continue;

        return (current | m_dtmIdent);  // make handle.
      }
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class DescendantOrSelfTraverser extends DescendantTraverser
  {

    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  To see if
     * the self node should be processed, use this function.
     *
     * @param context The context node of this traversal.
     *
     * @return the first node in the traversal.
     */
    public int first(int context)
    {
      return context;
    }

    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  To see if
     * the self node should be processed, use this function.  If the context
     * node does not match the extended type ID, this function will return
     * false.
     *
     * @param context The context node of this traversal.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the first node in the traversal.
     */
    public int first(int context, int extendedTypeID)
    {
      return (m_exptype[context & m_mask] == extendedTypeID)
             ? context : next(context, context, extendedTypeID);
    }
  }

  /**
   * Implements traversal of the entire subtree, including the root node.
   */
  private class AllFromNodeTraverser extends DescendantOrSelfTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      int subtreeRootIdent = context & m_mask;

      for (current = (current & m_mask) + 1; ; current++)
      {

        // Trickological code: _exptype() has the side-effect of
        // running nextNode until the specified node has been loaded,
        // and thus can be used to ensure that incremental construction of
        // the DTM has gotten this far. Using it just for that side-effect
        // is a bit of a kluge...
        _exptype(current);  // make sure it's here.

        if (!isDescendant(subtreeRootIdent, current))
          return NULL;

        return (current | m_dtmIdent);  // make handle.
      }
    }
  }

  /**
   * Implements traversal of the following access, in document order.
   */
  private class FollowingTraverser extends DescendantTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      int subtreeRootIdent = context & m_mask;

      if (context == current)
        current = getNextSibling(context) & m_mask;
      else
        current = (current & m_mask) + 1;

      for (; ; current++)
      {
        int type = _type(current);  // may call nextNode()

        if (NULL == type)
          return NULL;

        if (ATTRIBUTE_NODE == type || NAMESPACE_NODE == type)
          continue;

        return (current | m_dtmIdent);  // make handle.
      }
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {

      int subtreeRootIdent = context & m_mask;

      if (context == current)
        current = getNextSibling(context) & m_mask;
      else
        current = (current & m_mask) + 1;

      for (; ; current++)
      {
        int exptype = _exptype(current);  // may call nextNode()

        if (NULL == exptype)
          return NULL;

        if (exptype != extendedTypeID)
          continue;

        return (current | m_dtmIdent);  // make handle.
      }
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class FollowingSiblingTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {
      return getNextSibling(current);
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {

      while (DTM.NULL != (current = getNextSibling(current)))
      {
        if (m_exptype[current & m_mask] == extendedTypeID)
          return current;
      }

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class NamespaceDeclsTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      return (context == current)
             ? getFirstNamespaceNode(context, false)
             : getNextNamespaceNode(context, current, false);
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {

      current = (context == current)
                ? getFirstNamespaceNode(context, false)
                : getNextNamespaceNode(context, current, false);

      do
      {
        if (m_exptype[current] == extendedTypeID)
          return current;
      }
      while (DTM.NULL
             != (current = getNextNamespaceNode(context, current, false)));

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class NamespaceTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      return (context == current)
             ? getFirstNamespaceNode(context, true)
             : getNextNamespaceNode(context, current, true);
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {

      current = (context == current)
                ? getFirstNamespaceNode(context, true)
                : getNextNamespaceNode(context, current, true);

      do
      {
        if (m_exptype[current] == extendedTypeID)
          return current;
      }
      while (DTM.NULL
             != (current = getNextNamespaceNode(context, current, true)));

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class ParentTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      if (context == current)
        return m_parent[current & m_mask] | m_dtmIdent;
      else
        return NULL;
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {

      if (context != current)
        return NULL;

      current = current & m_mask;

      while (NULL != (current = m_parent[current]))
      {
        if (m_exptype[current] == extendedTypeID)
          return (current | m_dtmIdent);
      }

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class PrecedingTraverser extends DTMAxisTraverser
  {

    /**
     * Tell if the current identity is an ancestor of the context identity.
     * This is an expensive operation, made worse by the stateless traversal.
     * But the preceding axis is used fairly infrequently.
     *
     * @param contextIdent The context node of the axis traversal.
     * @param currentIdent The node in question.
     * @return true if the currentIdent node is an ancestor of contextIdent.
     */
    protected boolean isAncestor(int contextIdent, int currentIdent)
    {

      for (contextIdent = m_parent[contextIdent]; DTM.NULL != contextIdent;
              contextIdent = m_parent[contextIdent])
      {
        if (contextIdent == currentIdent)
          return true;
      }

      return false;
    }

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      int subtreeRootIdent = context & m_mask;

      for (current = (current & m_mask) - 1; current >= 0; current--)
      {
        int exptype = m_exptype[current];
        int type = ExpandedNameTable.getType(exptype);

        if (ATTRIBUTE_NODE == type || NAMESPACE_NODE == type
                || isAncestor(subtreeRootIdent, current))
          continue;

        return (current | m_dtmIdent);  // make handle.
      }

      return NULL;
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {

      int subtreeRootIdent = context & m_mask;

      for (current = (current & m_mask) - 1; current >= 0; current--)
      {
        int exptype = m_exptype[current];
        int type = ExpandedNameTable.getType(exptype);

        if (exptype != extendedTypeID
                || isAncestor(subtreeRootIdent, current))
          continue;

        return (current | m_dtmIdent);  // make handle.
      }

      return NULL;
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class PrecedingSiblingTraverser extends DTMAxisTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {
      return getPreviousSibling(current);
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {

      while (DTM.NULL != (current = getPreviousSibling(current)))
      {
        if (m_exptype[current & m_mask] == extendedTypeID)
          return current;
      }

      return NULL;
    }
  }

  /**
   * Implements traversal of the Self axis.
   */
  private class SelfTraverser extends DTMAxisTraverser
  {

    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  To see if
     * the self node should be processed, use this function.
     *
     * @param context The context node of this traversal.
     *
     * @return the first node in the traversal.
     */
    public int first(int context)
    {
      return context;
    }

    /**
     * By the nature of the stateless traversal, the context node can not be
     * returned or the iteration will go into an infinate loop.  To see if
     * the self node should be processed, use this function.  If the context
     * node does not match the extended type ID, this function will return
     * false.
     *
     * @param context The context node of this traversal.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the first node in the traversal.
     */
    public int first(int context, int extendedTypeID)
    {
      return (m_exptype[context & m_mask] == extendedTypeID) ? context : NULL;
    }

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return Always return NULL for this axis.
     */
    public int next(int context, int current)
    {
      return NULL;
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {
      return NULL;
    }
  }

  /**
   * A non-xpath axis, returns all nodes in the tree from and including the
   * root.
   */
  private class AllTraverser extends DTMAxisTraverser
  {

    /**
     * Return the root node.
     *
     * @param context The context node of this traversal.
     *
     * @return the first node in the traversal, which is the root node in the
     * DTM.
     */
    public int first(int context)
    {
      return getDocument();  // context is ignored.
    }

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      int subtreeRootIdent = context & m_mask;

      for (current = (current & m_mask) + 1; ; current++)
      {
        int type = _type(current);  // may call nextNode()

        if (NULL == type)
          return NULL;

        return (current | m_dtmIdent);  // make handle.
      }
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {

      int subtreeRootIdent = context & m_mask;

      for (current = (current & m_mask) + 1; ; current++)
      {
        int exptype = _exptype(current);  // may call nextNode()

        if (NULL == exptype)
          return NULL;

        if (exptype != extendedTypeID)
          continue;

        return (current | m_dtmIdent);  // make handle.
      }
    }
  }

  /**
   * Implements traversal of the Ancestor access, in reverse document order.
   */
  private class AllFromRootTraverser extends AllTraverser
  {

    /**
     * Return the root.
     *
     * @param context The context node of this traversal.
     *
     * @return the first node in the traversal.
     */
    public int first(int context)
    {
      return getDocument();
    }

    /**
     * Return the root if it matches the extended type ID.
     *
     * @param context The context node of this traversal.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the first node in the traversal.
     */
    public int first(int context, int extendedTypeID)
    {
      return (m_exptype[getDocument() & m_mask] == extendedTypeID)
             ? context : next(context, context, extendedTypeID);
    }
  }

  /**
   * Implements traversal of the Self axis.
   */
  private class RootTraverser extends AllFromRootTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return Always return NULL for this axis.
     */
    public int next(int context, int current)
    {
      return NULL;
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {
      return NULL;
    }
  }

  /**
   * A non-xpath axis, returns all nodes that aren't namespaces or attributes,
   * from and including the root.
   */
  private class DescendantFromRootTraverser extends AllFromRootTraverser
  {

    /**
     * Traverse to the next node after the current node.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current)
    {

      int subtreeRootIdent = context & m_mask;

      for (current = (current & m_mask) + 1; ; current++)
      {
        int type = _type(current);  // may call nextNode()

        if (NULL == type)
          return NULL;

        if (ATTRIBUTE_NODE == type || NAMESPACE_NODE == type)
          continue;

        return (current | m_dtmIdent);  // make handle.
      }
    }

    /**
     * Traverse to the next node after the current node that is matched
     * by the extended type ID.
     *
     * @param context The context node of this iteration.
     * @param current The current node of the iteration.
     * @param extendedTypeID The extended type ID that must match.
     *
     * @return the next node in the iteration, or DTM.NULL.
     */
    public int next(int context, int current, int extendedTypeID)
    {

      int subtreeRootIdent = context & m_mask;

      for (current = (current & m_mask) + 1; ; current++)
      {
        int exptype = _exptype(current);  // may call nextNode()

        if (NULL == exptype)
          return NULL;

        if (exptype != extendedTypeID)
          continue;

        return (current | m_dtmIdent);  // make handle.
      }
    }
  }
}
