import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './index.css';
import App from './features/home/App';
import Main from './features/home/Main';
// 사전 페이지
import Dict from './features/dictionary/DictPage';
import PlantData from './data/PlantData';
import PlantDetail from './features/dictionary/PlantDetail';
// 추천 페이지
import DarkHouse from './features/recommand/DarkHouse';
import LessWater from './features/recommand/LessWater';
import Beginner from './features/recommand/Beginner';
import Expert from './features/recommand/Expert';
// 등록
import Enroll from './features/enrollment/Pot_enroll';
import EnrollPlant from './features/enrollment/Plant_enroll';
// 업적 페이지
import Reward from './features/reward/reward';
import KakaoLogin from './features/login/KakaoLogin';




const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App/>}></Route>
        <Route path="/main/none" element={<Main/>}></Route>
        <Route path="/dictionary" element={<Dict/>}></Route>
        <Route path="/reward" element={<Reward/>}></Route>
        <Route path="/plantdata" element={<PlantData/>}></Route>
        <Route path="/dictionary/:plantId" element={<PlantDetail/>}></Route>
        {/* 해시태그 페이지 */}
        <Route path="/dictionary/darkhouse" element={<DarkHouse/>}></Route>
        <Route path="/dictionary/lesswater" element={<LessWater/>}></Route>
        <Route path="/dictionary/beginner" element={<Beginner/>}></Route>
        <Route path="/dictionary/expert" element={<Expert/>}></Route>
        <Route path="/kakaoLogin" element={<KakaoLogin/>}></Route>
        {/* 등록 */}
        <Route path="/enrollment" element={<Enroll/>}></Route>
        <Route path="/enrollment/plant" element={<EnrollPlant/>}></Route>
      </Routes>
    </BrowserRouter>

  </React.StrictMode>
);
