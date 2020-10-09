// selectors


const botBackBtn = document.querySelectorAll('.botBackBtn'); // назад из создать лобби
const validBackBtn = document.querySelectorAll('.validBackBtn'); // переход из списка лобби назад
const forms = document.querySelectorAll('form');
const nextButtonsActive = document.querySelectorAll('[data-active]');
const nextButtonsOnactive = document.querySelectorAll('[data-onactive]');
const categories = Array.from(document.querySelectorAll('.category'));
const validButtons = document.querySelectorAll('[data-valid-button]');
const inputs = Array.from(document.querySelectorAll('input, textarea'));
const refresh = document.querySelector('.refresh');
const buttons = Array.from(document.querySelectorAll('[data-side]'));
const container = document.querySelector('.container');
const lobbiesWrapper = document.querySelector('.lobbies');


let y = 0;


// functions

function preventDefault(e) {
  e.preventDefault();
}

function toggleActive(bool) {
  bool ? categories.map(category => category.classList.add('active'))
    : categories.map(category => category.classList.remove('active'))
}

function vibration(input, inputs, error) {
  input.classList.add('vibration', 'no-valid');
  input.previousElementSibling.style.display = 'block';
  input.previousElementSibling.textContent = error;
  setTimeout(() => {
    inputs.forEach(input => input.classList.remove('vibration'))
  }, 500);
}

function removeError(input) {
  input.classList.remove('vibration', 'no-valid');
  input.previousElementSibling.style.display = 'none';
}

function validation() {
  const inputs = this.closest('.side').querySelectorAll('input, textarea');
  let i = 0;
  inputs.forEach(input => {
      if (!input.value || input.value === ' ') {
        vibration(input, inputs, 'Заполните поле');
        return;
      } else if (input.classList.contains('number')) {
        if (input.value > 0) {
          removeError(input);
          console.log('+');
        } else {
          vibration(input, inputs, 'Число должно быть больше 0');
          console.log('-');
          return;
        }
      } else {
        removeError(input)
      }
      i++;
    }
  );
  if (i === inputs.length) {
    inputs.forEach(input => {
      input.classList.remove('vibration', 'no-valid');
      input.previousElementSibling.style.display = 'none';
    });
    if (this.dataset.request === 'present') {
      getPresent();
    } else {
      createLobby(inputs);
    }

    const side = this.dataset.side;
    container.style.transform = coords[side];
  }
  i = 0;
}

function createLobby(inputs) {
  const lobby = [];
  inputs.forEach(input => lobby.push(input.value));
  [ name, capacity ] = lobby;
  const data = {
    name,
    capacity
  };
  try {
    postData('rooms', data).then(response => {
      getLobby()
    })
  } catch (e) {
    console.error('Ошибка: ', e)
  }
}

let userId;

function getPresent() {
  const name = document.querySelector('.back input').value;
  const wishes = document.querySelector('.back textarea').value;
  const data = { name: name, wishes: wishes };
  try {
    postData('users', data).then(response => {
      joinLobby(response);
      userId = response.id;
    })
  } catch (e) {
    console.error('Ошибка:', e);
  }
}

function joinLobby({ id, name, wishes }) {
  let data = {
    id,
    name,
    wishes
  };
  let url = lobbyId ? `rooms/${ lobbyId }/users` : '/users/randomroom';
  try {
    postData(url, data, true).then(() => {
      santaSearch();
    })
  } catch (e) {
    console.log(e)
  }
}

const isEmptyObject = (obj) => (Object.getOwnPropertyNames(obj).length === 0);

function santaSearch() {
  let loader = document.createElement('div');
  loader.classList.add('loader');
  loader.innerHTML = '<div>Выбираемся из смирительной рубашки...</div>';
  container.before(loader);
  let side = document.querySelector('.left');
  let i = 0;
  let intervalCube = setInterval(() => {
    if (i > 10000) i = 0;
    i += 122;
    container.style.transform = `rotateY(${ i }deg)`;
  }, 200);
  let interval = setInterval(() => {
    getData(`users/${ userId }`).then(response => {
      if (isEmptyObject(response.recipient)) return;
      const { name, wishes } = response.recipient;
      clearInterval(interval);
      clearInterval(intervalCube);
      container.style.transform = coords.left;
      loader.style.display = 'none';
      side.innerHTML = `Вы дарите: ${ wishes } <br> Челу по имени: ${ name }`;
      setTimeout(() => {
        // side.classList.add('end');
      }, 200)
    })
  }, 5000)

}

function postData(sugar, data, space = false) {
  const url = `https://cft-secret-santa.herokuapp.com/api/v001/${ sugar }`;
  if (space) {
    return fetch(`${ url }`, {
      method: 'POST',
      body: JSON.stringify(data),
      headers: {
        'Content-Type': 'application/json'
      }
    })
  }
  return fetch(`${ url }`, {
    method: 'POST',
    body: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json'
    }
  }).then(data => data.json())
    .then(response => response)
}

function getData(sugar) {
  const url = `https://cft-secret-santa.herokuapp.com/api/v001/${ sugar }`;
  return fetch(`${ url }`)
    .then(data => data.json())
    .then(response => response)
}

function getRandomLobby() {

}

function getLobby() {
  try {
    getData('rooms').then(response => {
      renderLobbies(response)
    })
  } catch (e) {
    console.error('Ошибка:', e);
  }
}

function renderLobbies(lobbies) {
  const blocks = lobbies.map(lobby => {
    if (lobby.size && lobby.size === lobby.capacity) return;
    return `<div data-id="${ lobby.id }" class="lobby"><span>${ lobby.name }</span><span>${ lobby.size } из ${ lobby.capacity }</span></div>`
  }).join('');
  let lobbiesWrapper = document.querySelector('.lobbies');
  lobbiesWrapper.innerHTML = blocks;
}

function removeBorder() {
  this.classList.remove('no-valid');
  this.previousElementSibling.style.display = 'none';
}

const coords = {
  bottom: 'rotateX(90deg)',
  top: 'rotateX(-90deg)',
  front: '',
  right: 'rotateY(-90deg)',
  back: 'rotateY(-180deg)',
  left: 'rotateY(-270deg)'
};

function goTo() {
  if (this.dataset.validButton === 'true') return;
  if (this.dataset.side === 'bottom') getLobby();
  const side = this.dataset.side;
  container.style.transform = coords[side]
}

let lobbyId;

function chooseLobby(e) {
  if (!e.target.classList.contains('lobby') && e.target.tagName !== 'SPAN') return;
  let lobby = e.target;
  if (e.target.tagName === 'SPAN') lobby = lobby.closest('.lobby');
  lobbyId = lobby.dataset.id;
  container.style.transform = coords.back;
}

// refresh.addEventListener('click', getPresent);

lobbiesWrapper.addEventListener('click', chooseLobby);

inputs.forEach(input => input.addEventListener('focus', removeBorder));
validButtons.forEach(button => button.addEventListener('click', validation));

nextButtonsActive.forEach(button =>
  button.addEventListener('click', () => toggleActive(true)));
nextButtonsOnactive.forEach(button =>
  button.addEventListener('click', () => toggleActive(false)));

botBackBtn.forEach(button =>
  button.addEventListener('click', () => toggleActive(true)));
validBackBtn.forEach(button =>
  button.addEventListener('click', () => toggleActive(true)));

forms.forEach(form => {
  form.addEventListener('submit', preventDefault)
});

buttons.forEach(button => button.addEventListener('click', goTo));



