package bbj.graphicsobjects;

import bbj.*;
import bbj.virtualobjects.FreeComment;
import bbj.virtualobjects.LifeLine;
import bbj.virtualobjects.Message;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.*;

/**
 *
 * @author Alexander
 */
public class SceneItemListener implements MouseListener, MouseMotionListener/*
 * , KeyListener
 */ {

    public static SceneItem m_currentSelectedItem;       // Текущий выделенный объект
    private int m_startX;               // Координата начала перетаскивания
    private int m_startY;               // Координата конца перетаскивания
    private SceneItem m_selectedItem;     // Перетаскиваемый коммент
    private int m_startYOnLine;
    private int endLine1;
    private int endLine2;

    /**
     * Конструктор с параметрами (используется всеми элементами сцены)
     *
     * @param item Слушаемый элемент сцены
     */
    SceneItemListener(SceneItem item) {

        // Зануляем поля
        m_selectedItem = null;

        // Определяем тип текущего объекта и запоминаем его 
        if (item.getClass().getName().equals("bbj.graphicsobjects.UIFreeComment")) {
            m_selectedItem = (UIFreeComment) item;
        } else if (item.getClass().getName().equals("bbj.graphicsobjects.UIRectLifeLine")) {
            m_selectedItem = (UIRectLifeLine) item;
        } else if (item.getClass().getName().equals("bbj.graphicsobjects.UIActorLifeLine")) {
            m_selectedItem = (UIActorLifeLine) item;
        } else if (item.getClass().getName().equals("bbj.graphicsobjects.UISimpleMessage")) {
            m_selectedItem = (UISimpleMessage) item;
        } else if (item.getClass().getName().equals("bbj.graphicsobjects.UICreateMessage")) {
            m_selectedItem = (UICreateMessage) item;
        }
        else if (item.getClass().getName().equals("bbj.graphicsobjects.UIDestroyMessage")){
            m_selectedItem = (UIDestroyMessage) item;
        }
        else if (item.getClass().getName().equals("bbj.graphicsobjects.UIAsynchronousMessage")){
            m_selectedItem = (UIAsynchronousMessage) item;
        }
        else if (item.getClass().getName().equals("bbj.graphicsobjects.UIFocusControl")){
            m_selectedItem = (UIFocusControl) item;
        }
        
    }

    /**
     * Конструктор по умолчанию (используется сценой)
     */
    SceneItemListener() {
        // Зануляем поля
        m_selectedItem = null;
        endLine1 = 220;
        endLine2 = 230;
    }

    /**
     * Действия при клике на объект
     *
     * @param e Данное событие мыши
     */
    @Override
    public void mouseClicked(MouseEvent e) {

        //Если кликнули больше одного раза
        if (e.getClickCount() > 1
                && m_selectedItem != null
                && e.getButton() == MouseEvent.BUTTON1) {
            m_selectedItem.edit(true);
            EditField f = new EditField(m_selectedItem);
            m_selectedItem.f = f;
            
//            for (int i = 0; i < BBJ.app.getScene().getModel().size(); i++) {
//                if (BBJ.app.getScene().getModel().getObject(i).getId() == m_selectedItem.id) {
//                    if (BBJ.app.getScene().getModel().getObject(i).getClass() == FreeComment.class) {
//                        BBJ.app.getScene().getModel().getObject(i).setDescription(m_selectedItem.getText());
//                    } else if (BBJ.app.getScene().getModel().getObject(i).getClass() == LifeLine.class) {
//                        ((LifeLine)BBJ.app.getScene().getModel().getObject(i)).setName(m_selectedItem.getText());
//                    } else {
//                        ((Message)BBJ.app.getScene().getModel().getObject(i)).setName(m_selectedItem.getText());
//                    }
//                }
//            }
        }
    }

    /**
     * Действие при перемещении объекта мышью
     *
     * @param e Данное событие мыши
     */
    @Override
    public void mouseDragged(MouseEvent e) {

        if (true) {// сделать проверку кода клавиши
            // Если объект существует
            if (m_selectedItem == null) {
                return;
            }
            int endX, endY;

            // Определяем координаты конца движения
            endX = e.getXOnScreen() - m_startX;

            // Проверим, что не вылезли за гграницы окна по Х
            endX = Math.max(endX, 0);
            endX = Math.min(endX, m_selectedItem.getParent().getWidth() - m_selectedItem.w);

            endY = e.getYOnScreen() - m_startY;

            // Проверим, что не вылезли за гграницы окна по Y
            endY = Math.max(endY, 0);

            endY = Math.min(endY, m_selectedItem.getParent().getHeight() - m_selectedItem.h);

            if (m_selectedItem.getClass().getSuperclass().getName().equals("bbj.graphicsobjects.UILifeLine")) {
                endY = m_selectedItem.y;


                int asd = e.getY();
                if (m_startYOnLine >= endLine1 && m_startYOnLine <= endLine2) {
                    changeLifeLineLength((UILifeLine) m_selectedItem, asd);
                }
            } else if (m_selectedItem.getClass().getSuperclass().getName().equals("bbj.graphicsobjects.UIMessage")) {

                // ограничиваем передвижение сообщения по оси У
                UIMessage currentMessage = (UIMessage) m_selectedItem;

                if (endY >= currentMessage.getSender().getHeight()-
                        currentMessage.m_focusSender.h) {
                    currentMessage.getSender().setLength(endY+
                            currentMessage.m_focusSender.h);
                }

                if (endY <= currentMessage.getSender().getY() + 70) {
                    endY = currentMessage.getSender().getY() + 70;
                }

                if (!currentMessage.getClass().getName().equals("bbj.graphicsobjects.UICreateMessage")) {
                    if (endY >= currentMessage.getReceiver().getHeight()-
                            currentMessage.m_focusReceiver.h) {
                        currentMessage.getReceiver().setLength(endY+
                            currentMessage.m_focusReceiver.h);
                    }

                    if (endY <= currentMessage.getReceiver().getY() + 70) {
                        endY = currentMessage.getReceiver().getY() + 70;
                    }
                } else {
                    if (endY - 25 <= currentMessage.m_receiver.dotCoord - currentMessage.m_receiver.y - 55) {
                        currentMessage.m_receiver.y = endY - 25;
                    } else {
                        endY = currentMessage.m_receiver.dotCoord - currentMessage.m_receiver.y - 30;
                    }
                }
            }
        
        if (m_selectedItem.getClass().getName().equals("bbj.graphicsobjects.UIDestroyMessage" )){
            
            UIMessage currentMessage = (UIMessage)m_selectedItem;
            
            if (currentMessage.m_receiver.h <= currentMessage.y+60)
                currentMessage.m_receiver.h = currentMessage.y+60;
        }

            Rectangle rect = BBJ.app.getScene().getUIPanelsRectangle();

            if (rect.contains(endX, endY)) {

                m_selectedItem.x = rect.x + rect.width + 2;
                m_selectedItem.y = rect.y + rect.height + 2;
            } else {
                
                if (m_selectedItem.getClass().getName().equals("bbj.graphicsobjects.UIFocusControl" )){
                    UIFocusControl fc = (UIFocusControl)m_selectedItem;
                    int res = endY-m_selectedItem.y+40;
                    
                    // Не даем прямоугольникам опуститься ниже линии жиззни
                    // У сендера
                    if (fc.m_isSender && endY <= fc.m_parentMessage.getSender().getHeight()-40
                            && endY >= fc.y-20)
                        m_selectedItem.h = res;
                    
                    // У ресивера
                    if (!fc.m_isSender && endY <= fc.m_parentMessage.getReceiver().getHeight()-40
                             && endY >= fc.y-20)
                        m_selectedItem.h = res;
                }
                else{
                    m_selectedItem.x = endX;
                    m_selectedItem.y = endY; 
                }
                
            }

            m_selectedItem.updateUI();     // Перерисовываем объект

            for (int i = 0; i < BBJ.app.getScene().getModel().size(); i++) {
                if (BBJ.app.getScene().getModel().getObject(i).getId() == m_selectedItem.id) {
                    BBJ.app.getScene().getModel().getObject(i).setCoordinates(new Point3D(m_selectedItem.x, m_selectedItem.y, 0));
                    break;
                }
            }
        }
        BBJ.app.m_hasModifications = true;
    }

    /**
     * Действия при нажатии на объект
     *
     * @param e Данное событие мыши
     */
    @Override
    public void mousePressed(MouseEvent e) {

        Scene scene = BBJ.app.getScene();
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!e.isControlDown()) {
                scene.getSelectedObjects().clear();

                // Снимаем выделение с прежнего объекта если он существует

                if (SceneItemListener.m_currentSelectedItem != null && !e.isControlDown()) {
                    ArrayList<SceneItem> itemList = scene.getSceneObjects();
                    Iterator it = itemList.iterator();
                    while (it.hasNext()) {// Очищаем все элементы от выделения
                        SceneItem cur = (SceneItem) it.next();
                        cur.select(false);
                        if (cur.isEdited()) {
                            cur.f.setVisible(false);
                            cur.remove(cur.f);
                        }
                    }

                }
                if (e.isControlDown() && m_selectedItem.isSelected()) {
                    m_selectedItem.select(false);  // Выделяем объект
                }

                // Если данный объект сцена
                if (m_selectedItem == null) {
                    scene.getSelectedObjects().clear();
                    return;

                }

                endLine1 = m_selectedItem.h - 75;
                endLine2 = m_selectedItem.h - 65;

                m_selectedItem.select(true);  // Выделяем объект

                // Запоминаем выделенный объект
                SceneItemListener.m_currentSelectedItem = m_selectedItem;
                scene.addToSelectedObjects(m_selectedItem);

                // Координаты щелчка мыши
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();

                m_startYOnLine = e.getY();

                // Вычисляем координаты начала перетаскивания
                this.m_startX = x - m_selectedItem.x;
                this.m_startY = y - m_selectedItem.y;
                //SceneItemListener.currentSelectedItem.repaint();    // Перерисовываем
                endLine1 = m_selectedItem.h - 25 - m_selectedItem.y;
                endLine2 = m_selectedItem.h - 15 - m_selectedItem.y;

                m_selectedItem.select(true);  // Выделяем объект
            } else {
                // Если данный объект не сцена
                if (m_selectedItem == null) {
                    scene.getSelectedObjects().clear();
                    return;

                }

                if (m_selectedItem.m_isSelected) {
                    for (int i = 0; i < scene.getSizeSelectedObjects(); i++) {
                        if (scene.getSelectedObjects().get(i).equals(m_selectedItem)) {
                            scene.getSelectedObjects().get(i).select(false);
                            scene.getSelectedObjects().remove(i);
                        }
                    }
                } else {

                    m_selectedItem.select(true);  // Выделяем объект
                    SceneItemListener.m_currentSelectedItem = m_selectedItem;
                }
            }

            if (!scene.getSelectedObjects().contains(m_selectedItem)) {
                scene.addToSelectedObjects(m_selectedItem);
            }
            scene.repaint();
        } else  if (e.getButton() == MouseEvent.BUTTON3) { 
            if (m_selectedItem.getClass() == UIRectLifeLine.class ||
                m_selectedItem.getClass() == UIActorLifeLine.class) {
                ((UILifeLine)m_selectedItem).m_menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /**
     * Изменить размер линии жизни
     *
     * @param line Сама ЛЖ
     * @param newY Новая координата конца линии
     */
    public void changeLifeLineLength(UILifeLine line, int newY) {
        if (newY >= 70) {
            boolean isOk = true;

            if (line.y > 50) {
                newY += line.y - 50;
            }


            Iterator<UIMessage> i = line.m_inbox.iterator();

                while(i.hasNext()){
                   UIMessage buf = i.next();
                   
                    if (buf.getClass().getName().equals("bbj.graphicsobjects.UIDestroyMessage" )){
                        if (newY <= buf.getY()+buf.getHeight()-10+buf.m_focusReceiver.h)
                        isOk = false;
                    }
                    else
                        if (newY <= buf.getY()+buf.getHeight()-50+buf.m_focusReceiver.h)
                            isOk = false;

                }
            

            if (isOk) {
                i = line.m_outbox.iterator();

                while (i.hasNext()) {
                    UIMessage buf = i.next();
                    if (newY <= buf.getY() + buf.getHeight() - 50+buf.m_focusSender.h) {
                        isOk = false;
                    }
                }
            }

            if (isOk) {
                line.h = newY + 70;
                line.dotCoord = line.h;
                line.setBounds(line.x, line.y, line.w, line.h);
            }
        }
        BBJ.app.m_hasModifications = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
