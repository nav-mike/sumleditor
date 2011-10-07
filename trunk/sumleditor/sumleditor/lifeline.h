#ifndef LIFELINE_H
#define LIFELINE_H

#include <QGraphicsItem>
#include <QRectF>
#include <QPainterPath>
#include "comfunctions.h"


/**
 * �����, ����������� ������ � ��� ����� �����.
 */
class LifeLine : public QGraphicsItem
{
public:
	/**
	 * ����������� �� ���������. 
	 * @param graphWidget ��������� �� ������, �� ������� ����� ��������.
	 */
	LifeLine(GraphWidget *graphWidget);

	/**
	 * ����������.
	 */
	~LifeLine();

	/** 
	 * ������� ������������� ������ ������.
	 * @return ������ ��������������.
	 */
	QRectF boundingRect() const;

	/** 
	 * ������� ����� ������.
	 * @return ������ �����.
	 */
	QPainterPath shape() const;

	/**
	 * ���������� ������.
	 * @param painter ���������� ��������.
	 * @param option �������� ���������.
	 * @param widget ������, �� ������� ������.
	 */
	void paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget = 0);

	/**
	 * ������������ ������������� ���� �������.
	 */
	enum { Type = 0 };

	/**
	 * ������� ������������� ���� �������.
	 * @return ������������� ���� �������.
	 */
    inline int type() const { return Type; }

	/**
	 * ������ ����� ��������� ������� �������� ��������� ������� �� �����.
	 */
	void getCurrentCoords();

	/**
	 * ������ ������� ���������� �� �����.
	 * @param newX ���������� �� X.
	 * @param newY ���������� �� Y.
	 * @param newZ ���������� �� Z.
	 */
	void setCoords(double newX, double newY, double newZ);

protected:

	/** 
	 * ������� ����� ������������� �� ������.
	 * @param ������� ����.
	 */
	void mousePressEvent(QGraphicsSceneMouseEvent *event);

private:
	/** ��������� �� ������, �� ������� ������. */
	GraphWidget		*graph;

	double x; /** ���������� �� ��� �. */

	double y; /** ���������� �� ��� y. */

	double z; /** ���������� �� ��� Z. */
};

#endif // LIFELINE_H