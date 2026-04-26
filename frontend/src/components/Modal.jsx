import React from 'react';
import './Modal.css';

const Modal = ({ isOpen, onClose, title, children }) => {
    if (!isOpen) return null;

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-container" onClick={(e) => e.stopPropagation()}>
                {title && <h2 className="modal-title">{title}</h2>}
                <div className="modal-content">
                    {children}
                </div>
                <div className="modal-actions">
                    <button className="modal-close-btn" onClick={onClose}>Закрыть</button>
                </div>
            </div>
        </div>
    );
};

export default Modal;