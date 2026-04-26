import React from 'react';
import './Skeleton.css';

class Skeleton extends React.Component {
    render() {
        let {width, height, borderRadius} = this.props;
        return (
            <div
                className="skeleton"
                style={{width, height, borderRadius}}
            />
        );
    }
}


Skeleton.defaultProps = {width: '100%', height: '20px', borderRadius: 'var(--radius-sm)'}

export default Skeleton;