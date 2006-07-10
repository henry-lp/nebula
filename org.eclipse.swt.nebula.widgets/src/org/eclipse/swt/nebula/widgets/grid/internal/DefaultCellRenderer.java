/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    chris.gross@us.ibm.com - initial API and implementation
 *******************************************************************************/ 
package org.eclipse.swt.nebula.widgets.grid.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.nebula.widgets.grid.Grid;
import org.eclipse.swt.nebula.widgets.grid.GridCellRenderer;
import org.eclipse.swt.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.nebula.widgets.grid.GridItem;
import org.eclipse.swt.nebula.widgets.grid.IInternalWidget;

public class DefaultCellRenderer extends GridCellRenderer
{

    int leftMargin = 4;

    int rightMargin = 4;

    int topMargin = 2;

    int bottomMargin = 2;

    private int insideMargin = 3;

    private ToggleRenderer toggleRenderer;

    private CheckBoxRenderer checkRenderer;

    /**
     * {@inheritDoc}
     */
    public void paint(GC gc, Object value)
    {
        GridItem item = (GridItem)value;

        gc.setFont(item.getFont(getColumn()));
        
        boolean drawAsSelected = isSelected();
        
        if (isCellSelected())
        {
            drawAsSelected = true;//(!isCellFocus());        
        }

        if (drawAsSelected)
        {
            gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
            gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
        }
        else
        {
            if (item.getParent().isEnabled())
            {
                gc.setBackground(item.getBackground(getColumn()));
            }
            else
            {
                gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
            }
            gc.setForeground(item.getForeground(getColumn()));
        }

        gc.fillRectangle(getBounds().x, getBounds().y, getBounds().width + 1,
                         getBounds().height + 1);

        
        int x = leftMargin;

        if (isTree())
        {
            x += getToggleIndent(item);

            toggleRenderer.setExpanded(item.isExpanded());

            toggleRenderer.setHover(getHoverDetail().equals("toggle"));

            toggleRenderer.setLocation(getBounds().x + x, (getBounds().height - toggleRenderer
                .getBounds().height)
                                                          / 2 + getBounds().y);
            if (item.hasChildren())
                toggleRenderer.paint(gc, null);

            x += toggleRenderer.getBounds().width + insideMargin;

        }

        if (isCheck())
        {

            checkRenderer.setChecked(item.getChecked(getColumn()));
            checkRenderer.setGrayed(item.getGrayed(getColumn()));
            if (!item.getParent().isEnabled())
            {
                checkRenderer.setGrayed(true);
            }
            checkRenderer.setHover(getHoverDetail().equals("check"));

            checkRenderer.setBounds(getBounds().x + x, (getBounds().height - checkRenderer
                .getBounds().height)
                                                       / 2 + getBounds().y, checkRenderer
                .getBounds().width, checkRenderer.getBounds().height);
            checkRenderer.paint(gc, null);

            x += checkRenderer.getBounds().width + insideMargin;
        }

        int width = getBounds().width - x - rightMargin;

        String text = TextUtils.getShortString(gc, item.getText(getColumn()), width);

        if (getAlignment() == SWT.RIGHT)
        {
            int len = gc.stringExtent(text).x;
            if (len < width)
            {
                x += width - len;
            }
        }
        else if (getAlignment() == SWT.CENTER)
        {
            int len = gc.stringExtent(text).x;
            if (len < width)
            {
                x += (width - len) / 2;
            }
        }

        if (drawAsSelected)
        {
            gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
        }
        else
        {
            gc.setForeground(item.getForeground(getColumn()));
        }

        gc.drawString(text, getBounds().x + x, getBounds().y + topMargin, true);

        if (item.getParent().getLinesVisible())
        {
            if (isCellSelected())
            {
                //XXX: should be user definable?
                gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
            }
            else
            {
                gc.setForeground(item.getParent().getLineColor());
            }
            gc.drawLine(getBounds().x, getBounds().y + getBounds().height, getBounds().x
                                                                           + getBounds().width,
                        getBounds().y + getBounds().height);
            gc.drawLine(getBounds().x + getBounds().width - 1, getBounds().y, getBounds().x
                                                                              + getBounds().width
                                                                              - 1,
                        getBounds().y + getBounds().height);
        }
        
        if (isCellFocus())
        {
//            Grid grid = item.getParent();
//            GridColumn column = grid.getColumn(getColumn());
//            
//            Point cell = new Point(grid.indexOf(grid.getPreviousVisibleColumn(column)),grid.indexOf(item));
//            
//            boolean left = !grid.isCellSelected(cell);
//            
//            cell = new Point(grid.indexOf(grid.getNextVisibleColumn(column)),grid.indexOf(item));
//            
//            boolean right = !grid.isCellSelected(cell);
//            
//            cell = new Point(getColumn(),grid.indexOf(grid.getPreviousVisibleItem(item)));
//            
//            boolean top = !grid.isCellSelected(cell);
//            
//            cell = new Point(getColumn(),grid.indexOf(grid.getNextVisibleItem(item)));
//            
//            boolean bottom = !grid.isCellSelected(cell);

            
            Rectangle focusRect = new Rectangle(getBounds().x -1, getBounds().y - 1, getBounds().width,
                                                getBounds().height + 1);
            
            gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
            gc.drawRectangle(focusRect);
            
//            if (top) gc.drawLine(focusRect.x,focusRect.y,focusRect.x + focusRect.width,focusRect.y);
//            if (bottom) gc.drawLine(focusRect.x,focusRect.y + focusRect.height,focusRect.x + focusRect.width,focusRect.y + focusRect.height);
//            if (left) gc.drawLine(focusRect.x,focusRect.y,focusRect.x,focusRect.y + focusRect.height);
//            if (right) gc.drawLine(focusRect.x + focusRect.width,focusRect.y,focusRect.x + focusRect.width,focusRect.y + focusRect.height);
//            
            if (isFocus())
            {
                focusRect.x ++;
                focusRect.width -= 2;
                focusRect.y ++;
                focusRect.height -= 2;
                
                gc.drawRectangle(focusRect);

//                if (top) gc.drawLine(focusRect.x,focusRect.y + 1,focusRect.x + focusRect.width,focusRect.y + 1);
//                if (bottom) gc.drawLine(focusRect.x,focusRect.y + focusRect.height -1,focusRect.x + focusRect.width,focusRect.y + focusRect.height -1);
//                if (left) gc.drawLine(focusRect.x + 1,focusRect.y,focusRect.x + 1,focusRect.y + focusRect.height);
//                if (right) gc.drawLine(focusRect.x + focusRect.width -1,focusRect.y,focusRect.x + focusRect.width -1,focusRect.y + focusRect.height);
//                
            }
        }
        else if (isCellFocus())
        {
            Rectangle focusRect = new Rectangle(getBounds().x -1, getBounds().y - 1, getBounds().width,
                                                getBounds().height + 1);
            
            gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));

            gc.drawLine(focusRect.x,focusRect.y,focusRect.x + focusRect.width,focusRect.y);
            gc.drawLine(focusRect.x,focusRect.y + focusRect.height,focusRect.x + focusRect.width,focusRect.y + focusRect.height);
            gc.drawLine(focusRect.x,focusRect.y,focusRect.x,focusRect.y + focusRect.height);
            gc.drawLine(focusRect.x + focusRect.width,focusRect.y,focusRect.x + focusRect.width,focusRect.y + focusRect.height);
            
            if (isFocus())
            {
                gc.drawLine(focusRect.x,focusRect.y + 1,focusRect.x + focusRect.width,focusRect.y + 1);
                gc.drawLine(focusRect.x,focusRect.y + focusRect.height -1,focusRect.x + focusRect.width,focusRect.y + focusRect.height -1);
                gc.drawLine(focusRect.x + 1,focusRect.y,focusRect.x + 1,focusRect.y + focusRect.height);
                gc.drawLine(focusRect.x + focusRect.width -1,focusRect.y,focusRect.x + focusRect.width -1,focusRect.y + focusRect.height);
            }
        }

    }

    public Point computeSize(GC gc, int wHint, int hHint, Object value)
    {
        GridItem item = (GridItem)value;

        gc.setFont(item.getFont(getColumn()));

        int x = 0;

        x += leftMargin;

        if (isTree())
        {
            x += getToggleIndent(item);

            x += toggleRenderer.getBounds().width + insideMargin;

        }

        if (isCheck())
        {
            x += checkRenderer.getBounds().width + insideMargin;
        }

        x += gc.stringExtent(item.getText(getColumn())).x + rightMargin;

        int y = 0;

        y += topMargin;

        y += gc.getFontMetrics().getHeight();

        y += bottomMargin;

        return new Point(x, y);
    }

    public boolean notify(int event, Point point, Object value)
    {

        GridItem item = (GridItem)value;

        if (isCheck())
        {
            if (event == IInternalWidget.HOVER)
            {
                if (overCheck(item, point))
                {
                    setHoverDetail("check");
                    return true;
                }
            }

            if (event == IInternalWidget.CLICK)
            {
                if (overCheck(item, point))
                {
                    item.setChecked(getColumn(), !item.getChecked(getColumn()));
                    item.getParent().redraw();

                    item.fireCheckEvent(getColumn());

                    return true;
                }
            }
        }

        if (isTree() && item.hasChildren())
        {
            if (event == IInternalWidget.HOVER)
            {
                if (overToggle(item, point))
                {
                    setHoverDetail("toggle");
                    return true;
                }
            }

            if (event == IInternalWidget.CLICK)
            {
                if (overToggle(item, point))
                {
                    item.setExpanded(!item.isExpanded());
                    item.getParent().redraw();

                    if (item.isExpanded())
                    {
                        item.fireEvent(SWT.Expand);
                    }
                    else
                    {
                        item.fireEvent(SWT.Collapse);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    private boolean overCheck(GridItem item, Point point)
    {

        point = new Point(point.x, point.y);
        point.x -= getBounds().x - 1;
        point.y -= getBounds().y - 1;

        int x = leftMargin;
        if (isTree())
        {
            x += getToggleIndent(item);
            x += toggleRenderer.getSize().x + insideMargin;
        }

        if (point.x >= x && point.x < (x + checkRenderer.getSize().x))
        {
            int yStart = ((getBounds().height - checkRenderer.getBounds().height) / 2);
            if (point.y >= yStart && point.y < yStart + checkRenderer.getSize().y)
            {
                return true;
            }
        }

        return false;
    }

    private int getToggleIndent(GridItem item)
    {
        return item.getLevel() * 20;
    }

    private boolean overToggle(GridItem item, Point point)
    {

        point = new Point(point.x, point.y);

        point.x -= getBounds().x - 1;
        point.y -= getBounds().y - 1;

        int x = leftMargin;
        x += getToggleIndent(item);

        if (point.x >= x && point.x < (x + toggleRenderer.getSize().x))
        {
            // return true;
            int yStart = ((getBounds().height - toggleRenderer.getBounds().height) / 2);
            if (point.y >= yStart && point.y < yStart + toggleRenderer.getSize().y)
            {
                return true;
            }
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.mozart.mwt.widgets.table.internal.TableCellRenderer#setTree(boolean)
     */
    public void setTree(boolean tree)
    {
        super.setTree(tree);

        if (tree)
        {
            toggleRenderer = new ToggleRenderer();
            toggleRenderer.setDisplay(getDisplay());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.mozart.mwt.widgets.table.internal.TableCellRenderer#setCheck(boolean)
     */
    public void setCheck(boolean check)
    {
        super.setCheck(check);

        if (check)
        {
            checkRenderer = new CheckBoxRenderer();
            checkRenderer.setDisplay(getDisplay());
        }
        else
        {
            checkRenderer = null;
        }
    }

}
