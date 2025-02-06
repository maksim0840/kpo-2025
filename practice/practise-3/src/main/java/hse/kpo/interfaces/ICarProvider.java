package hse.kpo.interfaces;

import hse.kpo.domains.Car;
import hse.kpo.domains.Customer;

/**
 * Интерфейс сервиса управления автомобилей
 */
public interface ICarProvider {
    /**
     * Поиск подходящего под покупателя автомобиля
     *
     * @param customer - покупатель, под которого подбираем автомобиль
     * @return  optional на подходящий автомобиль (которого может не оказаться)
     */
    Car takeCar(Customer customer); // Метод возвращает optional на Car, что означает, что метод может ничего не вернуть
}
